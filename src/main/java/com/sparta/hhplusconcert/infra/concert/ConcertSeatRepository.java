package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import java.util.List;

public interface ConcertSeatRepository {
  List<ConcertSeatEntity> getSeatByScheduleId(Long scheduleId);

  ConcertSeatEntity getSeatById(Long id);

  List<ConcertSeatEntity> getSeatsById(List<Long> ids);

  Long updateSeat(ConcertSeatEntity concertSeat);

  Integer saveAll(List<ConcertSeatEntity> concertSeat);
}
