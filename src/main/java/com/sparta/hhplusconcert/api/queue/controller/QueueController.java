package com.sparta.hhplusconcert.api.queue.controller;

import com.sparta.hhplusconcert.api.queue.response.CheckQueueResponse;
import com.sparta.hhplusconcert.common.config.ApiResponse;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueueController {
  //대기열 요청

  @GetMapping("/{userId}")
  public ApiResponse<CheckQueueResponse> checkQueue(@PathVariable Long userId) {
    return ApiResponse.created(new CheckQueueResponse("dsaiuo2310923.213oiasjf09a3ji.ido12u890U#!",
        LocalDateTime.now(),
        30,
        Time.valueOf("00:00:45")));
  }
}
