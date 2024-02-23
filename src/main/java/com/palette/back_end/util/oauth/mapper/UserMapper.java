package com.palette.back_end.util.oauth.mapper;

import com.palette.back_end.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserMapper {
  public static User of(Map<String, Object> attributes) {
    return User.builder()
        .userName((String) attributes.get("userName"))
        .email((String) attributes.get("email"))
        .profilePictureUrl((String) attributes.get("picture"))
        .build();
  }

  public static User of(OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();
    return User.builder()
        .userId((Long) attributes.get("userId"))
        .email((String) attributes.get("email"))
        .build();
  }
}
