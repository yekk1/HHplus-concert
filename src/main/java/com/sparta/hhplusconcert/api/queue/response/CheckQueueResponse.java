package com.sparta.hhplusconcert.api.queue.response;

import java.sql.Time;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckQueueResponse {
  private String token;
  private LocalDateTime expireTime;
  private Integer remainNumber;
  private Time remainTime;

  @Builder
  public CheckQueueResponse(String token, LocalDateTime expireTime, Integer remainNumber,
      Time remainTime) {
    this.token = token;
    this.expireTime = expireTime;
    this.remainNumber = remainNumber;
    this.remainTime = remainTime;
  }
}
