package com.sparta.hhplusconcert.concert.usecase;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.sparta.hhplusconcert.concert.domain.ReservationStatus;
import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReserveSeatServiceTest {

  @Mock
  private ConcertSeatRepositoryImpl concertSeatRepository;

  @Mock
  private ConcertReservationRepositoryImpl concertReservationRepository;

  @InjectMocks
  private ReserveSeatService reserveSeatService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  @Transactional
  void testReserveSeatSuccessfully() {
    // Given
    Long seatId = 1L;
    Long userId = 2L;
    ReserveSeatService.Input input = ReserveSeatService.Input.builder()
        .seatId(seatId)
        .userId(userId)
        .build();

    // Seat 상태가 EMPTY인 가상의 좌석 생성
    ConcertSeatEntity seat = ConcertSeatEntity.builder()
        .id(seatId)
        .concertId(1L)
        .scheduleId(1L)
        .seatNumber(10)
        .status(SeatStatus.EMPTY)
        .build();

    // 예약 정보가 없는 경우
    List<ConcertReservationEntity> reservations = Collections.emptyList();

    // 저장될 ConcertReservationEntity 객체 생성
    ConcertReservationEntity reservation = ConcertReservationEntity.builder()
        .seatId(seatId)
        .userId(userId)
        .status(ReservationStatus.PENDING_PAYMENT)
        .expiredTime(LocalDateTime.now().plusMinutes(5))
        .build();

    when(concertSeatRepository.getSeatById(seatId)).thenReturn(seat);
    when(concertReservationRepository.getReservationsBySeatId(seatId)).thenReturn(reservations);
    when(concertReservationRepository.saveSeatReservation(any(ConcertReservationEntity.class))).thenReturn(1L);
    when(concertSeatRepository.updateSeat(any(ConcertSeatEntity.class))).thenReturn(seatId);

    // When
    ReserveSeatService.Output result = reserveSeatService.reserve(input);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getReservationId()).isEqualTo(1L);
    assertThat(result.getSeatId()).isEqualTo(seatId);

    verify(concertSeatRepository, times(1)).getSeatById(seatId);
    verify(concertReservationRepository, times(1)).getReservationsBySeatId(seatId);
    verify(concertReservationRepository, times(1)).saveSeatReservation(any(ConcertReservationEntity.class));
    verify(concertSeatRepository, times(1)).updateSeat(any(ConcertSeatEntity.class));
  }

  @Test
  @Transactional
  void testReserveSeatAlreadyTaken() {
    // Given
    Long seatId = 1L;
    Long userId = 2L;
    ReserveSeatService.Input input = ReserveSeatService.Input.builder()
        .seatId(seatId)
        .userId(userId)
        .build();

    // Seat 상태가 RESERVED인 가상의 좌석 생성
    ConcertSeatEntity seat = ConcertSeatEntity.builder()
        .id(seatId)
        .concertId(1L)
        .scheduleId(1L)
        .seatNumber(10)
        .status(SeatStatus.RESERVED)
        .build();

    // 예약 정보가 있는 경우
    ConcertReservationEntity existingReservation = ConcertReservationEntity.builder()
        .seatId(seatId)
        .userId(userId)
        .status(ReservationStatus.PAYMENT_COMPLETED)
        .expiredTime(LocalDateTime.now().plusMinutes(5))
        .build();

    List<ConcertReservationEntity> reservations = Arrays.asList(existingReservation);

    when(concertSeatRepository.getSeatById(seatId)).thenReturn(seat);
    when(concertReservationRepository.getReservationsBySeatId(seatId)).thenReturn(reservations);

    // When
    Throwable thrown = catchThrowable(() -> reserveSeatService.reserve(input));

    // Then
    assertThat(thrown).isInstanceOf(RuntimeException.class)
        .hasMessage("이미 선택 된 좌석입니다.");

    verify(concertSeatRepository, times(1)).getSeatById(seatId);
    verify(concertReservationRepository, times(1)).getReservationsBySeatId(seatId);
    verify(concertReservationRepository, times(0)).saveSeatReservation(any(ConcertReservationEntity.class));
    verify(concertSeatRepository, times(0)).updateSeat(any(ConcertSeatEntity.class));
  }
}