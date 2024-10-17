package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertScheduleEntity;
import java.util.List;

public interface ConcertScheduleRepository {
  List<ConcertScheduleEntity> getScheduleByConcertId(Long concertId);

}
