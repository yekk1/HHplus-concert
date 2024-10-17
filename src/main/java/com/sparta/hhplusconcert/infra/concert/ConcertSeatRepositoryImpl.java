package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository{
  private final ConcertSeatJpaRepository concertSeatJpaRepository;
  @Override
  public List<ConcertSeatEntity> getSeatByScheduleId(Long scheduleId){
    return concertSeatJpaRepository.findByScheduleId(scheduleId);
  }
}
