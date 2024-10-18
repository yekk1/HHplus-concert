package com.sparta.hhplusconcert.api.queue.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQueueResponse {
  private String queueToken;
  private Integer remainPosition;
}
