package com.sparta.hhplusconcert.point.usecase;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sparta.hhplusconcert.concert.domain.ReservationStatus;
import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.point.domain.PointTransactionType;
import com.sparta.hhplusconcert.point.domain.entity.PaymentEntity;
import com.sparta.hhplusconcert.point.domain.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepositoryImpl;
import com.sparta.hhplusconcert.point.infra.PaymentRepositoryImpl;
import com.sparta.hhplusconcert.point.infra.PointHistoryRepositoryImpl;
import com.sparta.hhplusconcert.point.infra.UserRepositoryImpl;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class PaySeatServiceTest {

  @Mock
  private ConcertSeatRepositoryImpl concertSeatRepository;

  @Mock
  private ConcertReservationRepositoryImpl concertReservationRepository;

  @Mock
  private UserRepositoryImpl userRepository;

  @Mock
  private PointHistoryRepositoryImpl pointHistoryRepository;

  @Mock
  private PaymentRepositoryImpl paymentRepository;

  @InjectMocks
  private PaySeatService paySeatService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this); // Mockito 초기화
  }

  @Test
  void testPay_SuccessfulPayment() {
    // Given
    Long reservationId = 1L;
    Long seatId = 2L;
    Long userId = 3L;
    Long amount = 500L;
    LocalDateTime futureTime = LocalDateTime.now().plusMinutes(5);

    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .id(reservationId)
        .status(ReservationStatus.PENDING_PAYMENT)
        .expiredTime(futureTime)
        .build();

    UserEntity user = UserEntity.builder()
        .id(userId)
        .userUuid(UUID.randomUUID())
        .point(1000L)
        .build();

    ConcertSeatEntity seat = ConcertSeatEntity.builder()
        .id(seatId)
        .status(SeatStatus.EMPTY)
        .build();

    PointHistoryEntity pointHistory = PointHistoryEntity.builder()
        .userId(userId)
        .amount(user.getPoint()-amount)
        .type(PointTransactionType.USE)
        .build();

    PaymentEntity payment = PaymentEntity.builder().id(10L).build();

    when(concertReservationRepository.getReservationById(reservationId)).thenReturn(reservation);
    when(userRepository.getUserData(userId)).thenReturn(user);
    when(userRepository.userPoint(user)).thenReturn(userId);
    when(pointHistoryRepository.chargePoint(pointHistory)).thenReturn(1L);
    when(concertSeatRepository.getSeatById(seatId)).thenReturn(seat);
    when(paymentRepository.generatePayment(any(PaymentEntity.class))).thenReturn(10L);
    when(concertSeatRepository.updateSeat(any(ConcertSeatEntity.class))).thenReturn(seatId);
    when(concertReservationRepository.saveSeatReservation(any(ConcertReservationEntity.class))).thenReturn(reservationId);

    // When
    PaySeatService.Input input = PaySeatService.Input.builder()
        .reservationId(reservationId)
        .seatId(seatId)
        .userId(userId)
        .amount(amount)
        .build();
    PaySeatService.Output result = paySeatService.pay(input);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getPaymentId()).isEqualTo(10L);

    verify(concertReservationRepository, times(1)).getReservationById(reservationId);
    verify(userRepository, times(1)).getUserData(userId);
    verify(userRepository, times(1)).userPoint(user);
    verify(pointHistoryRepository, times(1)).chargePoint(any());
    verify(paymentRepository, times(1)).generatePayment(any(PaymentEntity.class));
    verify(concertSeatRepository, times(1)).updateSeat(any(ConcertSeatEntity.class));
  }

  @Test
  void testPay_ExpiredPayment() {
    // Given
    Long reservationId = 1L;
    Long userId = 3L;
    Long seatId = 2L;
    Long amount = 500L;
    LocalDateTime pastTime = LocalDateTime.now().minusMinutes(5);

    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .id(reservationId)
        .status(ReservationStatus.PENDING_PAYMENT)
        .expiredTime(pastTime)
        .build();

    when(concertReservationRepository.getReservationById(reservationId)).thenReturn(reservation);

    // When/Then
    PaySeatService.Input input = PaySeatService.Input.builder()
        .reservationId(reservationId)
        .seatId(seatId)
        .userId(userId)
        .amount(amount)
        .build();

    assertThatThrownBy(() -> paySeatService.pay(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("결제시간이 만료되었습니다.");
  }

  @Test
  void testPay_NotPendingStatus() {
    // Given
    Long reservationId = 1L;
    Long userId = 3L;
    Long seatId = 2L;
    Long amount = 500L;

    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .id(reservationId)
        .status(ReservationStatus.PAYMENT_COMPLETED) // 이미 결제 완료 상태
        .build();

    when(concertReservationRepository.getReservationById(reservationId)).thenReturn(reservation);

    // When/Then
    PaySeatService.Input input = PaySeatService.Input.builder()
        .reservationId(reservationId)
        .seatId(seatId)
        .userId(userId)
        .amount(amount)
        .build();

    assertThatThrownBy(() -> paySeatService.pay(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("해당 좌석이 결제 대기 상태가 아닙니다.");
  }

  @Test
  void testPay_InsufficientPoints() {
    // Given
    Long reservationId = 1L;
    Long userId = 3L;
    Long seatId = 2L;
    Long amount = 500L;
    LocalDateTime futureTime = LocalDateTime.now().plusMinutes(5);

    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .id(reservationId)
        .status(ReservationStatus.PENDING_PAYMENT)
        .expiredTime(futureTime)
        .build();
    UserEntity user = UserEntity.builder()
        .id(userId)
        .point(200L) // 포인트 부족
        .build();

    when(concertReservationRepository.getReservationById(reservationId)).thenReturn(reservation);
    when(userRepository.getUserData(userId)).thenReturn(user);

    // When/Then
    PaySeatService.Input input = PaySeatService.Input.builder()
        .reservationId(reservationId)
        .seatId(seatId)
        .userId(userId)
        .amount(amount)
        .build();

    assertThatThrownBy(() -> paySeatService.pay(input))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("결제 포인트가 모자랍니다.");
  }
}