package com.sparta.hhplusconcert.api.concert.controller;

import com.sparta.hhplusconcert.api.concert.request.PaySeatRequest;
import com.sparta.hhplusconcert.api.concert.request.ReserveSeatRequest;
import com.sparta.hhplusconcert.api.concert.response.GetConcertDatesResponse;
import com.sparta.hhplusconcert.api.concert.response.GetConcertSeatsResponse;
import com.sparta.hhplusconcert.common.config.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/concert")
public class ConcertController {

  //예약가능 날짜 조회
  @GetMapping("/{concertId}")
  public ApiResponse<List<GetConcertDatesResponse>> getConcertDates(@PathVariable Long concertId) {
    List<GetConcertDatesResponse> responses = new ArrayList<>();

    GetConcertDatesResponse date1 = new GetConcertDatesResponse(1L, 1L, LocalDate.of(2024, 10, 1), "자리있음");
    GetConcertDatesResponse date2 = new GetConcertDatesResponse(2L, 1L, LocalDate.of(2024, 10, 2), "만석");

    responses.add(date1);
    responses.add(date1);

    return ApiResponse.ok(responses);
  }

  //좌석 조회
  @GetMapping("/{concertId}/{date}")
  public ApiResponse<List<GetConcertSeatsResponse>> getConcertSeats(
      @PathVariable Long concertId
      , @PathVariable Long date) {

    List<GetConcertSeatsResponse> responses = new ArrayList<>();

    GetConcertSeatsResponse seat1 = new GetConcertSeatsResponse(1L,1L,1L,1,"예약중");
    GetConcertSeatsResponse seat2 = new GetConcertSeatsResponse(2L,1L,1L,3,"비어있음");

    responses.add(seat1);
    responses.add(seat2);

    return ApiResponse.ok(responses);
  }

  //좌석 예약
  @PostMapping("/{userId}/reserve")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<Long> reserveSeat(
      @PathVariable Long userId
      , @Valid @RequestBody ReserveSeatRequest request
  ) {
    Long reserveId = 1L;
    return ApiResponse.created(reserveId);
  }

  //좌석 결제
  @PostMapping("/{userId}/payments")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<Long> paySeat(
      @PathVariable Long userId
      , @Valid @RequestBody PaySeatRequest request
  ) {
    Long paymentId = 1L;
    return ApiResponse.created(paymentId);
  }
}
