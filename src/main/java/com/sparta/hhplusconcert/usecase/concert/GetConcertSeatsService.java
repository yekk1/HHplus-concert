package com.sparta.hhplusconcert.usecase.concert;

import com.sparta.hhplusconcert.domain.concert.SeatStatus;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertSeatRepositoryImpl;
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
public class GetConcertSeatsService {
  private final ConcertSeatRepositoryImpl concertSeatRepository;

  @Getter
  public static class Input {
    Long scheduleId;
  }

  @Data
  @Builder
  public static class Output {
    Long seatId;
    Integer seatNumber;
    SeatStatus status;
  }
  //예약 가능한 콘서트 좌석 조회
  // 리스트로 반납

  public List<Output> get(Input input) {
    List<ConcertSeatEntity> concertSeats = concertSeatRepository.getSeatByScheduleId(input.getScheduleId());

    return concertSeats.stream()
        .map(seat -> Output.builder()
            .seatId(seat.getId())
            .seatNumber(seat.getSeatNumber())
            .status(seat.getStatus())
            .build())
        .collect(Collectors.toList());
  }
}
