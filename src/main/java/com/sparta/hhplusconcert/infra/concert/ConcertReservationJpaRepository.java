package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertReservationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {
  List<ConcertReservationEntity> findBySeatId(long seatId);
}
