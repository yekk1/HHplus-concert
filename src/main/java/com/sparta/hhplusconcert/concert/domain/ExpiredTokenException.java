package com.sparta.hhplusconcert.concert.domain;

public class ExpiredTokenException extends RuntimeException {
  public ExpiredTokenException(String message) {
    super(message);
  }
}
