package com.palette.back_end.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithMockCustomUser {
  String username() default "user";
  long userId() default 1L;
}
