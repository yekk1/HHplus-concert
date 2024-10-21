package com.sparta.hhplusconcert.queue.api.controller;

import com.sparta.hhplusconcert.queue.api.response.CreateQueueResponse;
import com.sparta.hhplusconcert.common.config.ApiResponse;
import com.sparta.hhplusconcert.queue.usecase.CreateJWTQueueTokenService;
import com.sparta.hhplusconcert.queue.usecase.CreateJWTQueueTokenService.Input;
import com.sparta.hhplusconcert.queue.usecase.GetRemainingQueueService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/queues")
public class QueueController {
  private final CreateJWTQueueTokenService createJWTQueueTokenService;
  private final GetRemainingQueueService getRemainingQueueService;
  @PostMapping("/token/generate")
  public ApiResponse<CreateQueueResponse> createQueue(
      @RequestHeader("X-USER-UUID") UUID userUuid
  ) {
    log.debug("QueueController#createQueue called.");
    log.debug("userUuid={}", userUuid);

    String token = createJWTQueueTokenService.create(Input.builder().userUuid(userUuid).build()).getToken();
    Integer position = getRemainingQueueService.get(token);

    CreateQueueResponse createQueueResponse = CreateQueueResponse.builder()
        .queueToken(token)
        .remainPosition(position)
        .build();
    log.debug("createQueueResponse={}", createQueueResponse);

    return ApiResponse.created(createQueueResponse);
  }

  @GetMapping("/token")
  public ApiResponse<Integer> checkQueuePosition(
      @RequestHeader("X-USER-UUID") UUID userUuid
      , @RequestHeader("X-QUEUE-TOKEN") String token
  ) {
    log.debug("QueueController#checkQueuePosition called.");
    log.debug("userUuid={}", userUuid);
    log.debug("token={}", token);

    Integer position = getRemainingQueueService.get(token);

    //position
    // 0: 통과
    // n: 대기 순번
    log.debug("remainPosition={}", position);
    return ApiResponse.ok(position);
  }
}