package com.sparta.hhplusconcert.usecase.point;

import com.sparta.hhplusconcert.infra.point.UserRepositoryImpl;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPointService {
  private final UserRepositoryImpl userRepository;
  @Getter
  public static class Input {
    Long UserId;
  }

  @Data
  @Builder
  public static class Output{
    Long point;
  }

  public Output get(Input input) {
    Long point = userRepository.getPoint(input.getUserId());
    return Output.builder()
        .point(point)
        .build();
  }
}
