package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertScheduleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
  List<ConcertScheduleEntity> findByConcertId(Long concertId);
}
