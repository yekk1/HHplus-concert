package com.sparta.hhplusconcert.domain.queue;

public class InvalidTokenException extends RuntimeException{
  public InvalidTokenException(String message) {
    super(message);
  }
}
