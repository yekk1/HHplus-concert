package com.sparta.hhplusconcert.usecase.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertScheduleEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertScheduleRepositoryImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetConcertSchedulesService {
  private final ConcertScheduleRepositoryImpl concertScheduleRepository;

  @Getter
  @Builder
  public static class Input {
    private Long concertId;
  }

  @Data
  @Builder
  public static class Output {
    private Long scheduleId;
    private LocalDate date;
    private Integer seatCapacity;
    private Integer seatLeft;
  }

  public List<Output> get(Input input) {
    List<ConcertScheduleEntity> concertSchedules = concertScheduleRepository.getScheduleByConcertId(input.getConcertId());
    return concertSchedules.stream()
        .map(schedule -> Output.builder()
            .scheduleId(schedule.getId())
            .date(schedule.getDate())
            .seatCapacity(schedule.getSeatCapacity())
            .seatLeft(schedule.getSeatLeft())
            .build())
        .collect(Collectors.toList());  // Output 객체 리스트로 변환하여 반환
  }
}
