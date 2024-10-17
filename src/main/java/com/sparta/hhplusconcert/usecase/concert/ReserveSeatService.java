package com.sparta.hhplusconcert.usecase.concert;

import com.sparta.hhplusconcert.domain.concert.ReservationStatus;
import com.sparta.hhplusconcert.domain.concert.SeatStatus;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.infra.concert.ConcertSeatRepositoryImpl;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveSeatService {
  private final ConcertSeatRepositoryImpl concertSeatRepository;
  private final ConcertReservationRepositoryImpl concertReservationRepository;
  @Getter
  public static class Input {
    Long seatId;
    Long userId;
  }

  @Data
  @Builder
  public static class Output {
    Long reservationId;
    Long seatId;
  }

  @Transactional
  public Output reserve(Input input) {
    ConcertSeatEntity concertSeat = concertSeatRepository.getSeatById(input.seatId);
    List<ConcertReservationEntity> concertReservations = concertReservationRepository.getReservationsBySeatId(input.seatId);
    if(concertSeat.getStatus() == SeatStatus.EMPTY ||
        concertReservations.stream()
            .anyMatch(reservation ->
                reservation.getStatus() == ReservationStatus.PENDING_PAYMENT ||
                    reservation.getStatus() == ReservationStatus.PAYMENT_COMPLETED)
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
      System.err.println("이미 선택 된 좌석입니다.");
      return null;
    }
  }
}
