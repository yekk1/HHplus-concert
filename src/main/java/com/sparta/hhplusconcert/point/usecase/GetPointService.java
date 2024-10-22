package com.sparta.hhplusconcert.point.usecase;

import com.sparta.hhplusconcert.point.infra.UserRepository;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPointService {
  @Qualifier("User")
  private final UserRepository userRepository;
  @Getter
  @Builder
  public static class Input {
    private Long UserId;
  }

  @Data
  @Builder
  public static class Output{
    private Long point;
  }

  public Output get(Input input) {
    Long point = userRepository.getPoint(input.getUserId());
    return Output.builder()
        .point(point)
        .build();
  }
}
