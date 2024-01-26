package com.palette.back_end.util.exceptions;

import lombok.Getter;

@Getter
public class PaletteException extends RuntimeException {

  private final ErrorCode errorCode;

  public PaletteException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
