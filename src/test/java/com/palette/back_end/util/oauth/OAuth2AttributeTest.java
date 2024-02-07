package com.palette.back_end.util.oauth;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class OAuth2AttributeTest {

  @Test
  void given_google_when_requested_then_returnGoogleOAuth2Attribute() {
    // given
    String provider = "google";
    String attributeKey = "sud";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("name", "testName");
    attributes.put("email", "testEmail");
    attributes.put("picture", "testPicture");

    // when
    OAuth2Attribute result = OAuth2Attribute.of(provider, attributeKey, attributes);

    // then
    assert(result.getUserName()).equals("testName");
    assert(result.getEmail()).equals("testEmail");
    assert(result.getPicture()).equals("testPicture");
    assert(result.getAttributeKey()).equals(attributeKey);
    assert(result.getAttributes()).equals(attributes);
  }

  @Test
  void given_kakao_when_requested_then_returnKakaoOAuth2Attribute() {
    String provider = "kakao";
    String attributeKey = "id";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("kakao_account", new HashMap<String, Object>());

    @SuppressWarnings("unchecked")
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    kakaoAccount.put("profile", new HashMap<String, Object>());

    @SuppressWarnings("unchecked")
    Map<String, Object>  kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    kakaoProfile.put("nickname", "testName");
    kakaoProfile.put("email", "testEmail");
    kakaoProfile.put("profile_image_url", "testPicture");

    // when
    OAuth2Attribute result = OAuth2Attribute.of(provider, attributeKey, attributes);

    // when
    Map<String, Object> map = result.convertToMap();

    assert(map.get("userName")).equals("testName");
    assert(map.get("email")).equals("testEmail");
    assert(map.get("picture")).equals("testPicture");
    assert(map.get("attributeKey")).equals(attributeKey);
    assert(map.get("attributes")).equals(kakaoAccount);
  }

  @Test
  void given_naver_when_requested_then_returnNaverOAuth2Attribute() {
    // given
    String provider = "naver";
    String attributeKey = "response";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("response", new HashMap<String, Object>());

    @SuppressWarnings("unchecked")
    Map<String, Object> attributesNaver = (Map<String, Object>) attributes.get("response");
    attributesNaver.put("nickname", "testName");
    attributesNaver.put("email", "testEmail");
    attributesNaver.put("profile_image", "testPicture");

    // when
    OAuth2Attribute result = OAuth2Attribute.of(provider, attributeKey, attributes);

    // then
    assert(result.getUserName()).equals("testName");
    assert(result.getEmail()).equals("testEmail");
    assert(result.getPicture()).equals("testPicture");
    assert(result.getAttributeKey()).equals(attributeKey);
    assert(result.getAttributes()).equals(attributes.get("response"));
  }

  @Test
  void given_provider_when_convertToMap_thenReturnMap() {
    // given
    String provider = "google";
    String attributeKey = "sud";
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("name", "testName");
    attributes.put("email", "testEmail");
    attributes.put("picture", "testPicture");

    // when
    OAuth2Attribute result = OAuth2Attribute.of(provider, attributeKey, attributes);

    // when
    Map<String, Object> map = result.convertToMap();

    assert(map.get("userName")).equals("testName");
    assert(map.get("email")).equals("testEmail");
    assert(map.get("picture")).equals("testPicture");
    assert(map.get("attributeKey")).equals(attributeKey);
    assert(map.get("attributes")).equals(attributes);
  }
}