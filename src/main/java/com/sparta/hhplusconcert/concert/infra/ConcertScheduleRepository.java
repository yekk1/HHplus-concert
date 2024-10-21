package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertScheduleEntity;
import java.util.List;

public interface ConcertScheduleRepository {
  List<ConcertScheduleEntity> getScheduleByConcertId(Long concertId);

}
