package com.palette.back_end.util;

import com.palette.back_end.entity.User;
import org.junit.jupiter.api.Test;

class JwtTokenUtilsTest {

  private static final String SECRET_KEY = "ABCDJFILDJFLKDJFLKSADJFLD2352454";

  @Test
  void given_user_when_requested_then_generateTokenAndCheck() {
    // given
    User user = User.builder()
            .userId(1L)
            .email("test@test.com")
            .profilePictureUrl("Test Address")
            .build();

    // when
    String token = JwtTokenUtils.generateToken(user, SECRET_KEY);
    Long userId = JwtTokenUtils.getUserId(token, SECRET_KEY);
    String email = JwtTokenUtils.getEmail(token, SECRET_KEY);

    // then
    assert(token.length() > 10);
    assert(userId).equals(1L);
    assert(email).equals("test@test.com");
  }
}