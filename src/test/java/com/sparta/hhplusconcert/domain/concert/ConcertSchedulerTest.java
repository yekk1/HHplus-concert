package com.sparta.hhplusconcert.domain.concert;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.infra.concert.ConcertSeatRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ConcertSchedulerTest {

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
  void expireReservations_whenNoExpiredReservations_shouldLogNoExpiredReservations() {
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
  void expireReservations_whenExpiredReservationsPresent_shouldCancelReservationsAndUpdateSeats() {
    // Given
    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .seatId(1L)
        .status(ReservationStatus.PENDING_PAYMENT)
        .build();

    when(concertReservationRepository.getExpiredReservations(LocalDateTime.now()))
        .thenReturn(List.of(reservation));

    ConcertSeatEntity seat = ConcertSeatEntity.builder()
        .id(1L)
        .status(SeatStatus.RESERVED)
        .build();
    when(concertSeatRepository.getSeatsById(List.of(1L))).thenReturn(List.of(seat));

    // When
    concertScheduler.expireReservations();

    // Then
    assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVATION_CANCELED);
    assertThat(seat.getStatus()).isEqualTo(SeatStatus.EMPTY);
    verify(concertReservationRepository).saveAll(List.of(reservation));
    verify(concertSeatRepository).saveAll(List.of(seat));
  }

  @Test
  void expireReservations_whenSaveFails_shouldThrowRuntimeException() {
    // Given
    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .seatId(1L)
        .status(ReservationStatus.PENDING_PAYMENT)
        .build();

    when(concertReservationRepository.getExpiredReservations(LocalDateTime.now()))
        .thenReturn(List.of(reservation));

    ConcertSeatEntity seat = ConcertSeatEntity.builder()
        .id(1L)
        .status(SeatStatus.RESERVED)
        .build();
    when(concertSeatRepository.getSeatsById(List.of(1L))).thenReturn(List.of(seat));

    // Simulate save failure
    when(concertReservationRepository.saveAll(any())).thenReturn(0);
    when(concertSeatRepository.saveAll(any())).thenReturn(1); // Save succeeded for seat

    // When & Then
    assertThatThrownBy(() -> concertScheduler.expireReservations())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("자동 예약 취소를 실패했습니다.");
  }
}