package com.sparta.hhplusconcert.domain.concert.entity;

import com.sparta.hhplusconcert.domain.common.TimeBaseEntity;
import com.sparta.hhplusconcert.domain.concert.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CONCERT_RESERVATION")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConcertReservationEntity extends TimeBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Long id;

  private Long seatId;

  private Long userId;

  private ReservationStatus status;

  private LocalDateTime expiredTime;

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(this.expiredTime);
  }
}