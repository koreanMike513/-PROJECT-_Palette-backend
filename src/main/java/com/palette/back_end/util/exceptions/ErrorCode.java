package com.palette.back_end.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INVALID(HttpStatus.BAD_REQUEST, "INVALID"),

  NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND ERROR"),

  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED ERROR");

  private final HttpStatusCode code;

  private final String message;
}
