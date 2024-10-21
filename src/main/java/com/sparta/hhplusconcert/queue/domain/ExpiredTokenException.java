package com.sparta.hhplusconcert.queue.domain;

public class ExpiredTokenException extends RuntimeException {
  public ExpiredTokenException(String message) {
    super(message);
  }
}
