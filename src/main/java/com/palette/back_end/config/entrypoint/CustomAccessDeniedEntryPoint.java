package com.palette.back_end.config.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.back_end.util.dto.ResponseDTO;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.ErrorResponse;
import com.palette.back_end.util.exceptions.PaletteException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedEntryPoint implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ResponseDTO<ErrorResponse> resultResponse = ResponseDTO.error(ErrorResponse.of(new PaletteException(ErrorCode.UNAUTHORIZED)));

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(resultResponse));
  }
}
