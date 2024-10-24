package com.sparta.hhplusconcert.concert.domain;

import com.sparta.hhplusconcert.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidTokenException extends RuntimeException{
  private final ErrorCode errorCode;
}
