package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertScheduleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
  List<ConcertScheduleEntity> findByConcertId(Long concertId);
}
