package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.ReservationStatus;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertReservationEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertReservationRepositoryImpl implements ConcertReservationRepository{
  private final ConcertReservationJpaRepository concertReservationJpaRepository;

  @Override
  public ConcertReservationEntity getReservationById(Long id) {
    return concertReservationJpaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당하는 예약이 존재하지 않습니다. ID: " + id));
  }

  @Override
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public List<ConcertReservationEntity> getReservationsBySeatId(Long seatId) {
    return concertReservationJpaRepository.findBySeatId(seatId);
  }


//  @Override
//  public ConcertReservationEntity getReservationBySeatNumber(Integer seatNumber) {
//    return null;
//  }

  @Override
  public Long generateSeatReservation(ConcertReservationEntity concertReservation) {
    ConcertReservationEntity savedConcertReservation = concertReservationJpaRepository.save(concertReservation);
    return savedConcertReservation.getId();
  }

  @Override
  public Integer updateReservation(List<Long> ids, ReservationStatus status) {
    return null;
  }
}
