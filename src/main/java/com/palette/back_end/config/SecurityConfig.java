package com.palette.back_end.config;


import com.palette.back_end.config.entrypoint.CustomAccessDeniedEntryPoint;
import com.palette.back_end.config.entrypoint.CustomAuthenticationEntryPoint;
import com.palette.back_end.config.filter.JwtTokenFilter;
import com.palette.back_end.repository.UserRepository;

import com.palette.back_end.util.oauth.CustomOAuth2UserService;
import com.palette.back_end.util.oauth.OAuth2FailureHandler;
import com.palette.back_end.util.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Value("${jwt.secret.key}")
  private String key;

  private final UserRepository userRepository;

  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  private final OAuth2FailureHandler oAuth2FailureHandler;

  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults())
            .sessionManagement(s -> s
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(au -> au
                    .requestMatchers("api/v1", "/login").permitAll()
                    .anyRequest().authenticated())
            .exceptionHandling(ea -> ea
                    .accessDeniedHandler(new CustomAccessDeniedEntryPoint())
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
            .oauth2Login(oa -> oa
                .loginPage("/login")
                .authorizationEndpoint(oe -> oe
                    .baseUri("/oauth2/authorize"))
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint(ua -> ua
                    .userService(customOAuth2UserService)))
            .addFilterBefore(new JwtTokenFilter(key, userRepository), UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public ApplicationListener<AuthenticationSuccessEvent> successLogger() {
    return event -> log.info("success {}", event.getAuthentication());
  }
}
