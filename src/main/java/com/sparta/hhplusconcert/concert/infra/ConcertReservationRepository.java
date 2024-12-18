package com.sparta.hhplusconcert.concert.infra;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface ConcertReservationRepository {
  ConcertReservationEntity getReservationById(Long id);
  List<ConcertReservationEntity> getReservationsBySeatId(Long id);
  List<ConcertReservationEntity> getExpiredReservations(LocalDateTime currentTime);
//  ConcertReservationEntity getReservationBySeatNumber(Integer seatNumber);
  Long saveSeatReservation(ConcertReservationEntity concertReservation);
  Integer saveAll(List<ConcertReservationEntity> concertReservations);
//  Integer updateReservation(List<Long> ids, ReservationStatus status);
}
