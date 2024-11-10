package com.sparta.hhplusconcert.point.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.hhplusconcert.concert.domain.ReservationStatus;
import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepository;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepository;
import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import com.sparta.hhplusconcert.point.infra.PaymentRepository;
import com.sparta.hhplusconcert.point.infra.PointHistoryRepository;
import com.sparta.hhplusconcert.point.infra.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Transactional
class PaySeatServiceIntegrationTest {

  @Autowired
  private PaySeatService paySeatService;

  @Autowired
  private ConcertReservationRepository concertReservationRepository;

  @Autowired
  private ConcertSeatRepository concertSeatRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PointHistoryRepository pointHistoryRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  private ConcertReservationEntity concertReservation;
  private UserEntity user;
  private ConcertSeatEntity concertSeat;

  @BeforeEach
  void setUp() {
    UUID userUuid = UUID.randomUUID();

    concertSeat = ConcertSeatEntity.builder()
        .id(1L)
        .concertId(1L)
        .scheduleId(1L)
        .seatNumber(1)
        .status(SeatStatus.EMPTY)
        .build();

    concertReservation = ConcertReservationEntity.builder()
        .id(1L)
        .seatId(1L)
        .userId(null)
        .status(ReservationStatus.PENDING_PAYMENT)
        .expiredTime(LocalDateTime.now().plusMinutes(5))
        .build();

    user = UserEntity.builder()
        .id(1L)
        .userUuid(userUuid)
        .point(10_000L)
        .build();

  }

  @Test
  @DisplayName("좌석 결제 동시성 테스트")
  void testConcurrentPayment() throws InterruptedException {
    //given
    PaySeatService.Input input = PaySeatService.Input.builder()
        .reservationId(1L)
        .seatId(1L)
        .userId(1L)
        .amount(5_000L) // 유저 1L의 초기 재화: 10_000
        .build();

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);

    //when
    executor.submit(() -> {
      try {
        paySeatService.pay(input);
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        latch.countDown();
      }
    });

    executor.submit(() -> {
      try {
        paySeatService.pay(input);
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        latch.countDown();
      }
    });

    latch.await();
    concertReservation = concertReservationRepository.getReservationById(1L);
    concertSeat = concertSeatRepository.getSeatById(1L);
    user = userRepository.getUserData(1L);

    //then
    // 결제가 하나만 성공했는지 확인
    assertEquals(ReservationStatus.PAYMENT_COMPLETED, concertReservation.getStatus());
    assertEquals(SeatStatus.PAID, concertSeat.getStatus());
    assertEquals(5_000L, user.getPoint());

  }
}