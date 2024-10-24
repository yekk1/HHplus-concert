package com.sparta.hhplusconcert.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements  ErrorCode{

  EXPIRED_WAITING_TOKEN(HttpStatus.UNAUTHORIZED, "Waiting token has expired"),
  INVALID_WAITING_TOKEN(HttpStatus.UNAUTHORIZED, "Waiting token is invalid")
  ;

  private final HttpStatus httpStatus;
  private final String message;

}
