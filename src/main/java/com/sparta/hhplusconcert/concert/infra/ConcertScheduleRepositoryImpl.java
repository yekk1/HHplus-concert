package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertScheduleEntity;
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
