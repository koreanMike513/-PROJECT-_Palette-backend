package com.palette.back_end.util.exceptions;

import com.palette.back_end.util.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PaletteException.class)
  public ResponseEntity<?> handlePaletteException(PaletteException e) {
    log.error("Error occurs {}", e.getMessage());
    return ResponseEntity
        .status(e.getErrorCode().getCode())
        .body(ResponseDTO.error(ErrorResponse.of(e.getErrorCode())));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> handleBindExceptions(BindException e) {
    BindingResult bindingResult = e.getBindingResult();
    Map<String, String> result = new HashMap<>();

    String key;

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      key = fieldError.getField();
      if (key.lastIndexOf('.') != -1) {
        key = key.substring(key.lastIndexOf('.') + 1);
      }
      result.put(key, fieldError.getDefaultMessage());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ResponseDTO.error(ErrorResponse.of(result)));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleOtherExceptions(Exception e) {
    log.error("Error occurs {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ResponseDTO.error(ErrorResponse.of(e)));
  }
}
