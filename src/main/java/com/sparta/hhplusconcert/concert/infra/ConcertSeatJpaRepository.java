package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
  List<ConcertSeatEntity> findByScheduleId(Long scheduleId);
}
