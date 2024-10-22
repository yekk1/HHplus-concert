package com.sparta.hhplusconcert.concert.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWaitingTokenResponse {
  private String waitingToken;
  private Integer remainPosition;
}
