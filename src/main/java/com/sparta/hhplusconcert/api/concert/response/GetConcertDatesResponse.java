package com.sparta.hhplusconcert.api.concert.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetConcertDatesResponse {
  private Long id;
  private Long concertId;
  private LocalDate schedule;
  private Integer seatCapacity;
   private Integer seatLeft;
}
