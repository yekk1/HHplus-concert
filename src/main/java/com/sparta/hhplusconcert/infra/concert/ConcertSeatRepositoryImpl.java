package com.sparta.hhplusconcert.infra.concert;

import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository{
  private final ConcertSeatJpaRepository concertSeatJpaRepository;
  @Override
  public List<ConcertSeatEntity> getSeatByScheduleId(Long scheduleId){
    return concertSeatJpaRepository.findByScheduleId(scheduleId);
  }

  @Override
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public ConcertSeatEntity getSeatById(Long id) {
    return concertSeatJpaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당하는 좌석이 존재하지 않습니다. ID: " + id));
  }

  @Override
  public Long updateSeat(ConcertSeatEntity concertSeat) {
    ConcertSeatEntity savedConcertSeat = concertSeatJpaRepository.save(concertSeat);
    return savedConcertSeat.getId();
  }
}
