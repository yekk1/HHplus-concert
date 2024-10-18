package com.sparta.hhplusconcert.api.queue.controller;

import com.sparta.hhplusconcert.api.queue.response.CreateQueueResponse;
import com.sparta.hhplusconcert.common.config.ApiResponse;
import com.sparta.hhplusconcert.usecase.queue.CreateJWTQueueTokenService;
import com.sparta.hhplusconcert.usecase.queue.CreateJWTQueueTokenService.Input;
import com.sparta.hhplusconcert.usecase.queue.GetRemainingQueueService;
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
  @PostMapping("/token")
  public ApiResponse<CreateQueueResponse> createQueue(
      @RequestHeader("X-Uesr-UUID") UUID userUuid
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
      @RequestHeader("X-User-UUID") UUID userUuid
      , @RequestHeader("X-Queue-Token") String token
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