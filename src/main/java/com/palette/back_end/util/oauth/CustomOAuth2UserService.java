package com.palette.back_end.util.oauth;

import com.palette.back_end.entity.User;
import com.palette.back_end.repository.UserRepository;
import com.palette.back_end.util.oauth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    String provider = userRequest.getClientRegistration().getRegistrationId();
    String attributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

    OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributeKey, oAuth2User.getAttributes());
    Map<String, Object> attributes = oAuth2Attribute.convertToMap();

    User user = saveOrUpdate(attributes);
    attributes.put("userId", user.getUserId());

    return new DefaultOAuth2User(Collections.emptyList(), attributes, "email");
  }

  private User saveOrUpdate(Map<String, Object> attributes) {
    User user = userRepository.findByEmail((String) attributes.get("email"))
            .map(entity -> entity.update(entity.getEmail()))
            .orElseGet(() -> UserMapper.of(attributes));

    return userRepository.save(user);
  }
}
