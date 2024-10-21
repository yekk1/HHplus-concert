package com.sparta.hhplusconcert.point.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaySeatRequest {
  private Long reservationId;
  private Long seatId;
  private Long userId;
  private Long amount;
}
