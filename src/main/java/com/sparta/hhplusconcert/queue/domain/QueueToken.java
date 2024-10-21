package com.sparta.hhplusconcert.queue.domain;

import com.sparta.hhplusconcert.domain.common.Status;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;


public record QueueToken (
  UUID userId,
  String token,
  Status status,
  LocalDateTime issuedTime,
  LocalDateTime expiredTime) {

  @Builder
  public QueueToken {}
}
