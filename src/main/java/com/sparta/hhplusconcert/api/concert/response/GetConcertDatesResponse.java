package com.sparta.hhplusconcert.api.concert.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetConcertDatesResponse {
  private Long id;
  private Long concertId;
  private LocalDate date;
  private String status;

  @Builder
  public GetConcertDatesResponse(Long id, Long concertId, LocalDate date, String status) {
    this.id = id;
    this.concertId = concertId;
    this.date = date;
    this.status = status;
  }
}
