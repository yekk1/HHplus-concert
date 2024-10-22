package com.sparta.hhplusconcert.concert.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;


public record WaitingToken(
  UUID userId,
  String token,
  Status status,
  LocalDateTime issuedTime,
  LocalDateTime expiredTime) {

  @Builder
  public WaitingToken {}
}
