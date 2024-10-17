package com.sparta.hhplusconcert.usecase.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertScheduleEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertScheduleRepositoryImpl;
import com.sparta.hhplusconcert.usecase.concert.GetConcertSchedulesService.Output;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class GetConcertSchedulesServiceTest {

  @Mock
  private ConcertScheduleRepositoryImpl concertScheduleRepository;

  @InjectMocks
  private GetConcertSchedulesService getConcertSchedulesService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화
  }

  @Test
  void testGetConcertSchedules() {
    // Given
    Long concertId = 1L;
    GetConcertSchedulesService.Input input = new GetConcertSchedulesService.Input(concertId);

    // ConcertScheduleEntity 객체들 Mock 설정
    ConcertScheduleEntity schedule1 = ConcertScheduleEntity.builder()
        .id(1L)
        .date(LocalDate.of(2024, 1, 1))
        .seatCapacity(50)
        .seatLeft(20)
        .build();


    ConcertScheduleEntity schedule2 = ConcertScheduleEntity.builder()
        .id(2L)
        .date(LocalDate.of(2024, 1, 2))
        .seatCapacity(50)
        .seatLeft(30)
        .build();

    List<ConcertScheduleEntity> mockSchedules = Arrays.asList(schedule1, schedule2);

    // ConcertScheduleRepositoryImpl에서 mockSchedules가 반환되도록 설정
    when(concertScheduleRepository.getScheduleByConcertId(concertId)).thenReturn(mockSchedules);

    // When
    List<Output> result = getConcertSchedulesService.get(input);

    // Then
    assertThat(result).hasSize(2);

    assertThat(result.get(0).getScheduleId()).isEqualTo(1L);
    assertThat(result.get(0).getDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    assertThat(result.get(0).getSeatCapacity()).isEqualTo(50);
    assertThat(result.get(0).getSeatLeft()).isEqualTo(20);

    assertThat(result.get(1).getScheduleId()).isEqualTo(2L);
    assertThat(result.get(1).getDate()).isEqualTo(LocalDate.of(2024, 1, 2));
    assertThat(result.get(1).getSeatCapacity()).isEqualTo(50);
    assertThat(result.get(1).getSeatLeft()).isEqualTo(30);

    verify(concertScheduleRepository, times(1)).getScheduleByConcertId(concertId);
  }
}