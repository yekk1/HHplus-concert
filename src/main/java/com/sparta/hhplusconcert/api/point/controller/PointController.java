package com.sparta.hhplusconcert.api.point.controller;

import com.sparta.hhplusconcert.common.config.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

  // 잔액 충전
  @PostMapping("/{userId}/charge")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<Long> chargePoint(
      @PathVariable Long userId
      , @RequestBody long amount){
    Long chargeId = 1L;
    return ApiResponse.created(chargeId);
  }

  // 잔액 조회
  @GetMapping("{userId}")
  public ApiResponse<Integer> getPoint(@PathVariable Long userId) {
    Integer point = 10000;
    return ApiResponse.ok(point);
  }
}
