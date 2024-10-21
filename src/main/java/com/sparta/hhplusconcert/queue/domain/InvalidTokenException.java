package com.sparta.hhplusconcert.queue.domain;

public class InvalidTokenException extends RuntimeException{
  public InvalidTokenException(String message) {
    super(message);
  }
}
