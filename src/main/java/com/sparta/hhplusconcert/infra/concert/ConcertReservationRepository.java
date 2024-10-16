package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.ReservationStatus;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertReservationEntity;
import java.util.List;

public interface ConcertReservationRepository {
  ConcertReservationEntity getReservationById(Long id);
  ConcertReservationEntity getReservationBySeatNumber(Integer seatNumber);
  Long generateSeatReservation(ConcertReservationEntity entity);
  Integer updateReservation(List<Long> ids, ReservationStatus status);
}
