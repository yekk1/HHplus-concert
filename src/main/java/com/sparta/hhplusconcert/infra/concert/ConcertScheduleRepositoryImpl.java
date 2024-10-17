package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertScheduleEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository{
  private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
  @Override
  public List<ConcertScheduleEntity> getScheduleByConcertId(Long concertId){
    return concertScheduleJpaRepository.findByConcertId(concertId);
  }

}
