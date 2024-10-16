package com.sparta.hhplusconcert.domain.concert.entity;

import com.sparta.hhplusconcert.domain.common.TimeBaseEntity;
import com.sparta.hhplusconcert.domain.concert.SeatStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CONCERT_SEAT")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConcertSeatEntity extends TimeBaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seat_id")
  private Long id;

  private Long concertId;

  private Long schedulId;

  private Integer seatNumber;

  private SeatStatus status;

  private Long userId;

}
