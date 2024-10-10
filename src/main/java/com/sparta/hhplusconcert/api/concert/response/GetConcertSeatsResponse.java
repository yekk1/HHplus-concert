package com.sparta.hhplusconcert.api.concert.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetConcertSeatsResponse {
  private Long id;
  private Long concertId;
  private Long scheduleId;
  private Integer seatNumber;
  private String status;

  @Builder
  public GetConcertSeatsResponse(Long id, Long concertId, Long scheduleId, Integer seatNumber,
      String status) {
    this.id = id;
    this.concertId = concertId;
    this.scheduleId = scheduleId;
    this.seatNumber = seatNumber;
    this.status = status;
  }
}
