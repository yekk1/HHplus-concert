package com.sparta.hhplusconcert.concert.usecase;

import com.sparta.hhplusconcert.concert.domain.ReservationStatus;
import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepository;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveSeatService {

  @Qualifier("ConcertSeat")
  private final ConcertSeatRepository concertSeatRepository;
  @Qualifier("ConcertReservation")
  private final ConcertReservationRepository concertReservationRepository;
  @Getter
  @Builder
  public static class Input {
    private Long seatId;
    private Long userId;
  }

  @Data
  @Builder
  public static class Output {
    private Long reservationId;
    private Long seatId;
  }

  @Transactional
  public Output reserve(Input input) {
    ConcertSeatEntity concertSeat = concertSeatRepository.getSeatById(input.seatId);
    List<ConcertReservationEntity> concertReservations = concertReservationRepository.getReservationsBySeatId(input.seatId);
    if(concertSeat.getStatus() == SeatStatus.EMPTY ||
        concertReservations.stream()
            .anyMatch(reservation ->
                reservation.getStatus() != ReservationStatus.PENDING_PAYMENT &&
                    reservation.getStatus() != ReservationStatus.PAYMENT_COMPLETED)
    ) {
      ConcertReservationEntity concertReservation = ConcertReservationEntity.builder()
          .seatId(concertSeat.getId())
          .userId(input.userId)
          .status(ReservationStatus.PENDING_PAYMENT)
          .expiredTime(LocalDateTime.now().plusMinutes(5))
          .build();
      Long savedReservationId = concertReservationRepository.saveSeatReservation(concertReservation);

      //콘서트좌석 update
      concertSeat.setStatus(SeatStatus.RESERVED);
      Long savedSeatId = concertSeatRepository.updateSeat(concertSeat);
      return Output.builder()
          .reservationId(savedReservationId)
          .seatId(savedSeatId)
          .build();
    } else {
      throw new RuntimeException("이미 선택 된 좌석입니다.");
    }
  }
}
