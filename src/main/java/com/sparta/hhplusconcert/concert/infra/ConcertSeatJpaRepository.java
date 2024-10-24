package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
  List<ConcertSeatEntity> findByScheduleId(Long scheduleId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<ConcertSeatEntity> findById(Long id);
}
