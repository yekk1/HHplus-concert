package com.sparta.hhplusconcert.concert.api.controller;

import com.sparta.hhplusconcert.concert.api.response.CreateWaitingTokenResponse;
import com.sparta.hhplusconcert.concert.api.response.GetConcertDatesResponse;
import com.sparta.hhplusconcert.concert.api.response.GetConcertSeatsResponse;
import com.sparta.hhplusconcert.config.ApiResponse;
import com.sparta.hhplusconcert.concert.usecase.GetConcertSchedulesService;
import com.sparta.hhplusconcert.concert.usecase.GetConcertSeatsService;
import com.sparta.hhplusconcert.concert.usecase.ReserveSeatService;
import com.sparta.hhplusconcert.concert.usecase.CreateJWTWaitingTokenService;
import com.sparta.hhplusconcert.concert.usecase.CreateJWTWaitingTokenService.Input;
import com.sparta.hhplusconcert.concert.usecase.GetRemainingWaitingService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/concerts")
public class ConcertController {
  private final GetConcertSchedulesService getConcertSchedulesService;
  private final GetConcertSeatsService getConcertSeatsService;
  private final ReserveSeatService reserveSeatService;
  private final CreateJWTWaitingTokenService createJWTWaitingTokenService;
  private final GetRemainingWaitingService getRemainingWaitingService;

  @GetMapping("/schedules/{concertId}")
  public ApiResponse<List<GetConcertDatesResponse>> getConcertSchedules(@PathVariable Long concertId) {
    log.debug("ConcertController#getConcertSchedules called.");
    log.debug("concertId={}", concertId);
    List<GetConcertSchedulesService.Output> schedules = getConcertSchedulesService.get(GetConcertSchedulesService.Input.builder().concertId(concertId).build());

    List<GetConcertDatesResponse> responses = new ArrayList<>();
    schedules.forEach(schedule -> {
      GetConcertDatesResponse response = GetConcertDatesResponse.builder()
          .id(schedule.getScheduleId())
          .concertId(concertId)
          .schedule(schedule.getSchedule())
          .seatCapacity(schedule.getSeatCapacity())
          .seatLeft(schedule.getSeatLeft())
          .build();

      responses.add(response);
    });

    log.debug("responses={}", responses);

    return ApiResponse.ok(responses);
  }

  @GetMapping("/seats/{scheduleId}")
  public ApiResponse<List<GetConcertSeatsResponse>> getConcertSeats(
      @PathVariable Long scheduleId) {
    log.debug("ConcertController#getConcertSeats called.");
    log.debug("scheduleId={}", scheduleId);

    List<GetConcertSeatsService.Output> seats = getConcertSeatsService.get(GetConcertSeatsService.Input.builder().scheduleId(scheduleId).build());

    List<GetConcertSeatsResponse> responses = new ArrayList<>();
    seats.forEach(seat-> {
      GetConcertSeatsResponse response = GetConcertSeatsResponse.builder()
          .id(seat.getSeatId())
          .seatNumber(seat.getSeatNumber())
          .status(seat.getStatus().toString())
          .build();

      responses.add(response);
    });

    log.debug("responses={}", responses);

    return ApiResponse.ok(responses);
  }

  @PostMapping("/reserve/{seatId}")
  public ApiResponse<Long> reserveSeat(
      @PathVariable Long seatId
      , @Valid @RequestBody Long userId
  ) {
    log.debug("ConcertController#reserveSeat called.");
    log.debug("userId={}", userId);

    Long reservationId = reserveSeatService.reserve(ReserveSeatService.Input.builder().seatId(seatId).userId(userId).build()).getReservationId();

    log.debug("reservationId={}", reservationId);

    return ApiResponse.created(reservationId);
  }

  @PostMapping("/waiting-token")
  public ApiResponse<CreateWaitingTokenResponse> createWaitingToken(
      @RequestHeader("X-USER-UUID") UUID userUuid
  ) {
    log.debug("ConcertController#createWaitingToken called.");
    log.debug("userUuid={}", userUuid);

    String token = createJWTWaitingTokenService.create(Input.builder().userUuid(userUuid).build()).getToken();
    Integer position = getRemainingWaitingService.get(token);

    CreateWaitingTokenResponse createWaitingTokenResponse = CreateWaitingTokenResponse.builder()
        .waitingToken(token)
        .remainPosition(position)
        .build();
    log.debug("createTokenResponse={}", createWaitingTokenResponse);

    return ApiResponse.created(createWaitingTokenResponse);
  }

  @GetMapping("/waiting-position")
  public ApiResponse<Integer> checkWaitingPosition(
      @RequestHeader("X-USER-UUID") UUID userUuid
      , @RequestHeader("X-WAITING-TOKEN") String token
  ) {
    log.debug("ConcertController#checkWaitingPosition called.");
    log.debug("userUuid={}", userUuid);
    log.debug("token={}", token);

    Integer position = getRemainingWaitingService.get(token);

    //position
    // 0: 통과
    // n: 대기 순번
    log.debug("remainPosition={}", position);
    return ApiResponse.ok(position);
  }
}
