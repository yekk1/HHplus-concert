package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetConcertSeatsService {

  @Qualifier("ConcertSeat")
  private final ConcertSeatRepository concertSeatRepository;

  @Getter
  @Builder
  public static class Input {
    private Long scheduleId;
  }

  @Data
  @Builder
  public static class Output {
    private Long seatId;
    private Integer seatNumber;
    private SeatStatus status;
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
