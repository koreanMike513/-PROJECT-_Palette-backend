package com.palette.back_end.util.oauth;

import com.palette.back_end.entity.User;
import com.palette.back_end.util.JwtTokenUtils;
import com.palette.back_end.util.oauth.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  @Value("${oauth.redirect.url}")
  private String REDIRECT_URL;

  @Value("${jwt.secret.key}")
  private String SECRET_KEY;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    User user = UserMapper.of(oAuth2User.getAttributes());// user 바뀐다.
    String token = JwtTokenUtils.generateToken(user, SECRET_KEY); // string 으로 받는다

    response.sendRedirect(getRedirectionURI(token));
  }

  private String getRedirectionURI(String token) {
    return UriComponentsBuilder.fromUriString(REDIRECT_URL)
            .queryParam("token", token)
            .build()
            .toUriString();
  }
}
