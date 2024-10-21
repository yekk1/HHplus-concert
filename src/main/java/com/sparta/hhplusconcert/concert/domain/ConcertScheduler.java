package com.sparta.hhplusconcert.concert.domain;

import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertScheduler {
  private final ConcertSeatRepositoryImpl concertSeatRepository;
  private final ConcertReservationRepositoryImpl concertReservationRepository;
  //임시 예약 만료 처리 (5분)
  @Scheduled(fixedRate = 10_000)
  public void expireReservations() {
    //만료시간이 지나고 예약상태인 애들 가져 옴
    List<ConcertReservationEntity> expiredReservations = concertReservationRepository.getExpiredReservations(
        LocalDateTime.now());
    //예약취소로 업데이트
    Integer savedReservationsSize = 0;
    if (!expiredReservations.isEmpty()) {
      for (ConcertReservationEntity reservation : expiredReservations) {
        reservation.setStatus(ReservationStatus.RESERVATION_CANCELED);
      }
      savedReservationsSize = concertReservationRepository.saveAll(expiredReservations);
    } else {
      System.out.println("기간 만료 된 예약 건이 없습니다.");
    }
    //같은 seast_id의 좌석상태도 비어있음으로 업데이트
    List<Long> seatIds = expiredReservations.stream()
        .map(ConcertReservationEntity::getSeatId)
        .collect(Collectors.toList());
    List<ConcertSeatEntity> concertSeats = concertSeatRepository.getSeatsById(seatIds);
    for (ConcertSeatEntity concertSeat : concertSeats) {
      concertSeat.setStatus(SeatStatus.EMPTY);
    }
    Integer savedSeatsSize = concertSeatRepository.saveAll(concertSeats);

    if(savedReservationsSize != expiredReservations.size() || savedSeatsSize != concertSeats.size()){
      throw new RuntimeException("자동 예약 취소를 실패했습니다.");
    }
  }
}
