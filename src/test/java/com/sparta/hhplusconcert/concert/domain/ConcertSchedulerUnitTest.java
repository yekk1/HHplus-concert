package com.sparta.hhplusconcert.concert.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ConcertSchedulerUnitTest {

  @InjectMocks
  private ConcertScheduler concertScheduler;

  @Mock
  private ConcertSeatRepositoryImpl concertSeatRepository;

  @Mock
  private ConcertReservationRepositoryImpl concertReservationRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void 예약시간이_만료_된_예약이_없으면__좌석예약_상태를_업데이트하지_않는다() {
    // Given
    when(concertReservationRepository.getExpiredReservations(LocalDateTime.now()))
        .thenReturn(List.of());

    // When
    concertScheduler.expireReservations();

    // Then
    verify(concertReservationRepository, never()).saveAll(any());
    verify(concertSeatRepository, never()).saveAll(any());
  }

  @Test
  void 예약시간이_만료_된_예약은__좌석예약_상태를_업데이트_한다() {
    // Given
    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .id(1L)
        .seatId(1L)
        .userId(1L)
        .status(ReservationStatus.PENDING_PAYMENT)
        .expiredTime(LocalDateTime.now().minusMinutes(10))
        .build();

    when(concertReservationRepository.getExpiredReservations(any(LocalDateTime.class)))
        .thenReturn(List.of(reservation));

    ConcertSeatEntity seat = ConcertSeatEntity.builder()
        .id(1L)
        .concertId(1L)
        .scheduleId(1L)
        .seatNumber(10)
        .status(SeatStatus.RESERVED)
        .build();
    when(concertSeatRepository.getSeatsById(List.of(1L))).thenReturn(List.of(seat));

    // When
    concertScheduler.expireReservations();

    // Then
    assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVATION_CANCELED);
    assertThat(seat.getStatus()).isEqualTo(SeatStatus.EMPTY);

    // Verify
    verify(concertReservationRepository).saveAll(List.of(reservation));
    verify(concertSeatRepository).saveAll(List.of(seat));
  }
}