package com.palette.back_end.util.exceptions;

import com.palette.back_end.util.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PaletteException.class)
  public ResponseDTO<ErrorResponse> handlePaletteException(PaletteException e) {
    log.error("Error occurs {}", e.getMessage());
    return ResponseDTO.error(ErrorResponse.of(e.getErrorCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseDTO<ErrorResponse> handleOtherExceptions(Exception e) {
    log.error("Error occurs {}", e.getMessage());
    return ResponseDTO.error(ErrorResponse.of(e));
  }
}
