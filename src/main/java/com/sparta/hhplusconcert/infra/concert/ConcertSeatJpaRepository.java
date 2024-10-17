package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
  List<ConcertSeatEntity> findByScheduleId(Long scheduleId);
}
