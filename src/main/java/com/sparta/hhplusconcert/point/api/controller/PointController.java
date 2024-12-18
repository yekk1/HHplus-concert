package com.sparta.hhplusconcert.point.api.controller;

import com.sparta.hhplusconcert.point.api.request.PaySeatRequest;
import com.sparta.hhplusconcert.point.api.request.ChargePointRequest;
import com.sparta.hhplusconcert.config.ApiResponse;
import com.sparta.hhplusconcert.point.usecase.ChargePointService;
import com.sparta.hhplusconcert.point.usecase.GetPointService;
import com.sparta.hhplusconcert.point.usecase.PaySeatService;
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

  @PostMapping("/pay-seat")
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
