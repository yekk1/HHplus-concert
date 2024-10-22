package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertScheduleEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Qualifier("ConcertSchedule")
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository{
  private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
  @Override
  public List<ConcertScheduleEntity> getScheduleByConcertId(Long concertId){
    return concertScheduleJpaRepository.findByConcertId(concertId);
  }

}
