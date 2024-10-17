package com.sparta.hhplusconcert.usecase.point;

import com.sparta.hhplusconcert.domain.concert.ReservationStatus;
import com.sparta.hhplusconcert.domain.concert.SeatStatus;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.domain.concert.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.domain.point.PointTransactionType;
import com.sparta.hhplusconcert.domain.point.entity.PaymentEntity;
import com.sparta.hhplusconcert.domain.point.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.domain.point.entity.UserEntity;
import com.sparta.hhplusconcert.infra.concert.ConcertReservationRepositoryImpl;
import com.sparta.hhplusconcert.infra.concert.ConcertSeatRepositoryImpl;
import com.sparta.hhplusconcert.infra.point.PaymentRepositoryImpl;
import com.sparta.hhplusconcert.infra.point.PointHistoryRepositoryImpl;
import com.sparta.hhplusconcert.infra.point.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaySeatService {
  private final ConcertSeatRepositoryImpl concertSeatRepository;
  private final ConcertReservationRepositoryImpl concertReservationRepository;
  private final UserRepositoryImpl  userRepository;
  private final PointHistoryRepositoryImpl pointHistoryRepository;
  private final PaymentRepositoryImpl paymentRepository;

  @Getter
  @Builder
  public static class Input {
    Long reservationId;
    Long seatId;
    Long userId;
    Long amount;
  }

  @Data
  @Builder
  public static class Output {
    Long paymentId;
  }

  @Transactional
  public Output pay(Input input) {
    //예약 상태인지 확인
    ConcertReservationEntity concertReservation = concertReservationRepository.getReservationById(input.getReservationId());

    if(concertReservation.getStatus() != ReservationStatus.PENDING_PAYMENT) {
      throw new RuntimeException("해당 좌석이 결제 대기 상태가 아닙니다.");
    }
    if(concertReservation.getExpiredTime().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("결제시간이 만료되었습니다.");
    }

    //포인트 사용
    UserEntity user = userRepository.getUserData(input.getUserId());
    if(user.getPoint() < input.getAmount()) {
      throw new RuntimeException("결제 포인트가 모자랍니다.");
    }
    user.setPoint(user.getPoint() - input.getAmount());
    Long userId = userRepository.userPoint(user);

    PointHistoryEntity pointHistory = PointHistoryEntity.builder()
        .user_id(input.getUserId())
        .amount(input.getAmount())
        .type(PointTransactionType.USE)
        .build();
    Long savedHistoryId = pointHistoryRepository.chargePoint(pointHistory);

    //결제내역 작성
    PaymentEntity payment = PaymentEntity.builder()
        .reservationId(input.getReservationId())
        .userId(input.getUserId())
        .seatId(input.getSeatId())
        .amount(input.getAmount())
        .build();
    Long savedPaymentId = paymentRepository.generatePayment(payment);

    //예약상태 변경
    concertReservation.setStatus(ReservationStatus.PAYMENT_COMPLETED);
    Long savedReservId = concertReservationRepository.saveSeatReservation(concertReservation);

    ConcertSeatEntity concertSeat = concertSeatRepository.getSeatById(input.getSeatId());
    concertSeat.setStatus(SeatStatus.PAID);
    Long savedSeatId = concertSeatRepository.updateSeat(concertSeat);
    return Output.builder()
        .paymentId(savedPaymentId)
        .build();
  }

}
