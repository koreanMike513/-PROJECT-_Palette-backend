package com.palette.back_end.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO<T> {

  private static final String SUCCESS = "SUCCESS";

  private static final String ERROR = "ERROR";

  private final String code;

  private final T result;

  public static <T> ResponseDTO<T> success(T result) {
    return new ResponseDTO<>(SUCCESS, result);
  }

  public static <T> ResponseDTO<T> error(T ErrorResponse) {
    return new ResponseDTO<>(ERROR, ErrorResponse);
  }
}
