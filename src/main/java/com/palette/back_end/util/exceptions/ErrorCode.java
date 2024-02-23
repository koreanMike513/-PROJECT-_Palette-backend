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

  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED ERROR"),

  FILE_CONVERSION(HttpStatus.INTERNAL_SERVER_ERROR, "FILE CONVERSION ERROR"),

  AMAZON_S3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AMAZON SERVICE CONNECTION FAILED"),

  INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "INVALID FILE EXTENSION"),

  INVALID_FILE_EXTENSION_OR_SIZE(HttpStatus.BAD_REQUEST, "INVALID FILE EXTENSION OR WRONG FILE SIZE")
  ;

  private final HttpStatusCode code;

  private final String message;
}
