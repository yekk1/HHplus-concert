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
  public static class Input {
    Long concertId;
  }

  @Data
  @Builder
  public static class Output {
    Long scheduleId;
    LocalDate date;
    Integer seatCapacity;
    Integer seatLeft;
  }

  public List<Output> get(Input input) {
    List<ConcertScheduleEntity> concertSchedule = concertScheduleRepository.getScheduleByConcertId(input.getConcertId());
    return concertSchedule.stream()
        .map(schedule -> Output.builder()
            .scheduleId(schedule.getId())
            .date(schedule.getDate())
            .seatCapacity(schedule.getSeatCapacity())
            .seatLeft(schedule.getSeatLeft())
            .build())
        .collect(Collectors.toList());  // Output 객체 리스트로 변환하여 반환
  }
}
