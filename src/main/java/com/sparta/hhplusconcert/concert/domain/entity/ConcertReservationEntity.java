package com.sparta.hhplusconcert.concert.domain.entity;

import com.sparta.hhplusconcert.common.domain.TimeBaseEntity;
import com.sparta.hhplusconcert.concert.domain.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CONCERT_RESERVATION", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"seatId"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConcertReservationEntity extends TimeBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long seatId;

  private Long userId;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Column(updatable = false)
  private LocalDateTime expiredTime;

}