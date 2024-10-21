package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import java.util.List;

public interface ConcertSeatRepository {
  List<ConcertSeatEntity> getSeatByScheduleId(Long scheduleId);

  ConcertSeatEntity getSeatById(Long id);

  List<ConcertSeatEntity> getSeatsById(List<Long> ids);

  Long updateSeat(ConcertSeatEntity concertSeat);

  Integer saveAll(List<ConcertSeatEntity> concertSeat);
}
