package com.sparta.hhplusconcert.usecase.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.hhplusconcert.domain.concert.SeatStatus;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertSeatRepositoryImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class GetConcertSeatsServiceTest {

  @Mock
  private ConcertSeatRepositoryImpl concertSeatRepository;

  @InjectMocks
  private GetConcertSeatsService getConcertSeatsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testGetConcertSeats() {
    // Given
    Long scheduleId = 1L;
    GetConcertSeatsService.Input input = GetConcertSeatsService.Input.builder()
        .scheduleId(scheduleId)
        .build();

    ConcertSeatEntity seat1 = ConcertSeatEntity.builder()
        .id(1L)
        .concertId(1L)
        .scheduleId(1L)
        .seatNumber(10)
        .status(SeatStatus.EMPTY)
        .build();

    ConcertSeatEntity seat2 = ConcertSeatEntity.builder()
        .id(2L)
        .concertId(2L)
        .scheduleId(2L)
        .seatNumber(20)
        .status(SeatStatus.RESERVED)
        .build();

    List<ConcertSeatEntity> mockSeats = Arrays.asList(seat1, seat2);

    when(concertSeatRepository.getSeatByScheduleId(scheduleId)).thenReturn(mockSeats);

    // When
    List<GetConcertSeatsService.Output> result = getConcertSeatsService.get(input);

    // Then
    assertThat(result).hasSize(2);

    assertThat(result.get(0).getSeatId()).isEqualTo(1L);
    assertThat(result.get(0).getSeatNumber()).isEqualTo(10);
    assertThat(result.get(0).getStatus()).isEqualTo(SeatStatus.EMPTY);

    assertThat(result.get(1).getSeatId()).isEqualTo(2L);
    assertThat(result.get(1).getSeatNumber()).isEqualTo(20);
    assertThat(result.get(1).getStatus()).isEqualTo(SeatStatus.RESERVED);

    // Verify that the repository method was called with the correct scheduleId
    verify(concertSeatRepository, times(1)).getSeatByScheduleId(scheduleId);
  }
}