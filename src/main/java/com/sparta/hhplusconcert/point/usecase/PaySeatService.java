package com.sparta.hhplusconcert.point.usecase;

import com.sparta.hhplusconcert.concert.domain.ReservationStatus;
import com.sparta.hhplusconcert.concert.domain.SeatStatus;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertReservationEntity;
import com.sparta.hhplusconcert.concert.domain.entity.ConcertSeatEntity;
import com.sparta.hhplusconcert.concert.infra.ConcertReservationRepository;
import com.sparta.hhplusconcert.concert.infra.ConcertSeatRepository;
import com.sparta.hhplusconcert.point.domain.PointTransactionType;
import com.sparta.hhplusconcert.point.domain.entity.PaymentEntity;
import com.sparta.hhplusconcert.point.domain.entity.PointHistoryEntity;
import com.sparta.hhplusconcert.point.domain.entity.UserEntity;
import com.sparta.hhplusconcert.point.infra.PaymentRepository;
import com.sparta.hhplusconcert.point.infra.PointHistoryRepository;
import com.sparta.hhplusconcert.point.infra.UserRepository;
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
  private final ConcertSeatRepository concertSeatRepository;
  private final ConcertReservationRepository concertReservationRepository;
  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final PaymentRepository paymentRepository;

  @Getter
  @Builder
  public static class Input {
    private Long reservationId;
    private Long seatId;
    private Long userId;
    private Long amount;
  }

  @Data
  @Builder
  public static class Output {
    private Long paymentId;
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
    //가격검증 로직 추가
    user.setPoint(user.getPoint() - input.getAmount());
    Long userId = userRepository.userPoint(user);

    PointHistoryEntity pointHistory = PointHistoryEntity.builder()
        .userId(input.getUserId())
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
