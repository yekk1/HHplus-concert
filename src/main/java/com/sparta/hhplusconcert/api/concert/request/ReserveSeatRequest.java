package com.sparta.hhplusconcert.api.concert.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class ReserveSeatRequest {
  private String token;
  private Long concertId;
  private Long seatId;

  @Builder
  public ReserveSeatRequest(String token, Long concertId, Long seatId) {
    this.token = token;
    this.concertId = concertId;
    this.seatId = seatId;
  }
}
