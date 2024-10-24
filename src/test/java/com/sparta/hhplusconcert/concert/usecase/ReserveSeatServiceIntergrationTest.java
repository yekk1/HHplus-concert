package com.sparta.hhplusconcert.concert.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Transactional
class ReserveSeatServiceConcurrencyTest {

  @Autowired
  private ReserveSeatService reserveSeatService;


  @Autowired
  private ConcertSeatRepository concertSeatRepository;

  private ConcertSeatEntity concertSeat;

  @Test
  void testConcurrentReservation() throws InterruptedException {

    ReserveSeatService.Input input1 = ReserveSeatService.Input.builder()
        .seatId(5L)
        .userId(1L)
        .build();

    ReserveSeatService.Input input2 = ReserveSeatService.Input.builder()
        .seatId(5L)
        .userId(2L)
        .build();

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);
    List<Exception> exceptions = new ArrayList<>();

    executor.submit(() -> {
      try {
       reserveSeatService.reserve(input1);
      } catch (Exception e) {
        exceptions.add(e);
      } finally {
        latch.countDown();
      }
    });

    executor.submit(() -> {
      try {
        reserveSeatService.reserve(input2);
      } catch (Exception e) {
        exceptions.add(e);
      } finally {
        latch.countDown();
      }
    });

    latch.await(); // 모든 스레드가 완료될 때까지 대기


    //한 명의 예약이 성공했고, 이미 선택 된 좌석 에러가 발생했는지 확인
    concertSeat = concertSeatRepository.getSeatById(5L);

    assertEquals(SeatStatus.RESERVED, concertSeat.getStatus());
    assertEquals("이미 선택 된 좌석입니다.", exceptions.get(0).getMessage(), "예외 메시지가 일치해야 합니다.");
  }
}