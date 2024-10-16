package com.sparta.hhplusconcert.domain.queue;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private Long id;

  private UUID userUuid;

  private Long point;

}
