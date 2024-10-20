package com.sparta.hhplusconcert.api.point.controller;

import com.sparta.hhplusconcert.api.point.request.PaySeatRequest;
import com.sparta.hhplusconcert.api.point.request.ChargePointRequest;
import com.sparta.hhplusconcert.common.config.ApiResponse;
import com.sparta.hhplusconcert.usecase.point.ChargePointService;
import com.sparta.hhplusconcert.usecase.point.GetPointService;
import com.sparta.hhplusconcert.usecase.point.PaySeatService;
import jakarta.validation.Valid;
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
@RequestMapping("/v1/points")
public class PointController {
  private final ChargePointService chargePointService;
  private final PaySeatService paySeatService;
  private final GetPointService getPointService;

  @PostMapping("/charge")
  public ApiResponse<Long> chargePoint(@RequestBody ChargePointRequest request){
    log.debug("PointController#chargePoint called.");
    log.debug("ChargePointRequest={}", request);

    Long chargeId = chargePointService.charge(ChargePointService.Input.builder().userId(request.getUserId()).amount(request.getAmount()).build()).getPointHistoryId();
    log.debug("savedId={}", chargeId);

    return ApiResponse.created(chargeId);
  }

  @PostMapping("/payments")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<Long> paySeat(
      @Valid @RequestBody PaySeatRequest request
  ) {
    log.debug("PointController#PaySeatRequest called.");
    log.debug("PaySeatRequest={}", request);

    Long paymentId = paySeatService.pay(PaySeatService.Input.builder()
        .reservationId(request.getReservationId()).seatId(request.getSeatId()).userId(request.getUserId()).amount(request.getAmount())
        .build()).getPaymentId();
    log.debug("paymentId={}", paymentId);

    return ApiResponse.created(paymentId);
  }

  @GetMapping("/point/{userId}")
  public ApiResponse<Long> getPoint(@PathVariable Long userId) {
    log.debug("PointController#getPoint called.");
    log.debug("userId={}", userId);

    Long point = getPointService.get(GetPointService.Input.builder().UserId(userId).build()).getPoint();
    log.debug("point={}", point);

    return ApiResponse.ok(point);
  }

}
