package com.palette.back_end.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private HttpStatusCode code;

  private String message;

  public static ErrorResponse of(HttpStatusCode code, String message) {
    return new ErrorResponse(code, message);
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
  }

  public static ErrorResponse of(Exception e) {
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

  public static ErrorResponse of(Map<String, String> result) {
    String key = "";

    for (String keys : result.keySet()) {
      key = keys;
    }

    return new ErrorResponse(HttpStatus.BAD_REQUEST, result.get(key));
  }
}
