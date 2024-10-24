package com.sparta.hhplusconcert.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
  INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "User is not authenticated"),
  FORBIDDEN(HttpStatus.FORBIDDEN, "User does not have the necessary permissions"),
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource does not exist"),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed for this endpoint"),
  RESOURCE_CONFLICT(HttpStatus.CONFLICT, "Resource conflict occurred"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
