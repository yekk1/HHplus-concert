package com.sparta.hhplusconcert.api.concert.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaySeatRequest {
  private String token;
  private Long concertId;
  private Long seatId;
  private Integer amount;

  @Builder
  public PaySeatRequest(String token, Long concertId, Long seatId, Integer amount) {
    this.token = token;
    this.concertId = concertId;
    this.seatId = seatId;
    this.amount = amount;
  }
}
