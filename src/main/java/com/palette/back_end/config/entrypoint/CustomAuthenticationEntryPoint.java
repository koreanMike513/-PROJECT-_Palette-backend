package com.palette.back_end.config.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palette.back_end.util.dto.ResponseDTO;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.ErrorResponse;
import com.palette.back_end.util.exceptions.PaletteException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    PaletteException e = new PaletteException(ErrorCode.UNAUTHORIZED);
    ResponseDTO<ErrorResponse> resultResponse = ResponseDTO.error(ErrorResponse.of(e.getErrorCode()));

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(resultResponse));
  }
}
