package com.palette.back_end.util.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OAuth2Attribute {

  private String userName;

  private String email;

  private String attributeKey;

  private String picture;

  private Map<String, Object> attributes;

  public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
    return switch(provider) {
      case "kakao"  -> ofKakao(attributeKey, attributes);
      case "naver"  -> ofNaver(attributeKey, attributes);
      case "google" -> ofGoogle(attributeKey, attributes);
      default -> null;
    };
  }
  private static OAuth2Attribute ofKakao(String attributeKey, Map<String, Object> attributes) {
    @SuppressWarnings("unchecked")
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

    @SuppressWarnings("unchecked")
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuth2Attribute.builder()
            .userName((String) kakaoProfile.get("nickname"))
            .email((String) kakaoProfile.get("email"))
            .picture((String) kakaoProfile.get("profile_image_url"))
            .attributes(kakaoAccount)
            .attributeKey(attributeKey)
            .build();
  }
  private static OAuth2Attribute ofNaver(String attributeKey, Map<String, Object> attributes) {
    @SuppressWarnings("unchecked")
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuth2Attribute.builder()
            .userName((String) response.get("nickname"))
            .email((String) response.get("email"))
            .picture((String) response.get("profile_image"))
            .attributes(response)
            .attributeKey(attributeKey)
            .build();
  }
  private static OAuth2Attribute ofGoogle(String attributeKey, Map<String, Object> attributes) {
    return OAuth2Attribute.builder()
            .userName((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .attributeKey(attributeKey)
            .picture((String) attributes.get("picture"))
            .attributes(attributes)
            .build();
  }
  public Map<String, Object> convertToMap() {
    Map<String, Object> map = new HashMap<>();

    map.put("userName", userName);
    map.put("email", email);
    map.put("attributeKey", attributeKey);
    map.put("picture", picture);
    map.put("attributes", attributes);

    return map;
  }
}
