package com.sparta.hhplusconcert.concert.domain;

public class InvalidTokenException extends RuntimeException{
  public InvalidTokenException(String message) {
    super(message);
  }
}
