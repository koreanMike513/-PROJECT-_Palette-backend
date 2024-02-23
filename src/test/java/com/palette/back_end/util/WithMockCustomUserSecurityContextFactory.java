package com.palette.back_end.util;

import com.palette.back_end.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    User user = User.builder()
        .userId(1L)
        .email("test@email.com")
        .build();

    context.setAuthentication(new UsernamePasswordAuthenticationToken(user, "password", Collections.emptyList()));
    return context;
  }
}
