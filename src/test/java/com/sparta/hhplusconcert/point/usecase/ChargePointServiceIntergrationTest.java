package com.sparta.hhplusconcert.point.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import com.sparta.hhplusconcert.point.infra.PointHistoryRepository;
import com.sparta.hhplusconcert.point.infra.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Transactional
class ChargePointServiceIntergrationTest {

  @Autowired
  private ChargePointService chargePointService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PointHistoryRepository pointHistoryRepository;

  private UserEntity user;

  @Test
  void 포인트를_충전할_수_있다() throws InterruptedException {
    //given
    ChargePointService.Input input = ChargePointService.Input.builder()
        .userId(1L)
        .amount(5_000L) // 초기포인트: 10_000
        .build();


    //when
    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);

    // 두 개의 스레드에서 동시에 포인트 충전 시도
    executor.submit(() -> {
      try {
        chargePointService.charge(input);
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        latch.countDown();
      }
    });

    executor.submit(() -> {
      try {
        chargePointService.charge(input);
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        latch.countDown();
      }
    });

    latch.await();

    user = userRepository.getUserData(1L);
    //then
    // 최종 포인트는 15_000L 이어야 함
    assertEquals(15_000L, user.getPoint());
  }
}