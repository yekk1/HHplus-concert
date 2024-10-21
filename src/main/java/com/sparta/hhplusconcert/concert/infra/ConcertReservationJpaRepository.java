package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {
  List<ConcertReservationEntity> findBySeatId(long seatId);

  @Query("SELECT cr FROM ConcertReservationEntity cr WHERE cr.status = 'PENDING_PAYMENT' AND cr.expiredTime < :currentTime")
  List<ConcertReservationEntity> findExpiredReservations(@Param("currentTime") LocalDateTime currentTime);
}
