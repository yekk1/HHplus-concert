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
  private Long id;

  @Column(nullable = false)
  private Long concertId;

  @Column(nullable = false)
  private Long schedulId;

  @Column(nullable = false)
  private Integer seatNumber;

  private SeatStatus status;

}
