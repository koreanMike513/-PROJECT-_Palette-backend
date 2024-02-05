package com.palette.back_end.config.filter;

import com.palette.back_end.entity.User;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.util.JwtTokenUtils;
import com.palette.back_end.util.exceptions.ErrorCode;
import com.palette.back_end.util.exceptions.PaletteException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final String key;

  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith("Bearer ")) {
      log.error("[JwtTokenFilter.doFilterInternal]: Error Occurred Header is Empty");
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.split(" ")[1].trim();

    log.debug("Token: {}", token);
    log.debug("Is token expired: {}", JwtTokenUtils.isExpired(token, key));


    if (JwtTokenUtils.isExpired(token, key)) {
      log.error("[JwtTokenFilter.doFilterInternal]: Error Occurred key has expired");
      filterChain.doFilter(request, response);

      return;
    }

    Long userId = JwtTokenUtils.getUserId(token, key);
    User user = userRepository.findById(userId).orElseThrow(() -> new PaletteException(ErrorCode.NOT_FOUND));
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
