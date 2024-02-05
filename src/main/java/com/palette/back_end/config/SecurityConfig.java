package com.palette.back_end.config;

import com.palette.back_end.config.entrypoint.CustomAccessDeniedEntryPoint;
import com.palette.back_end.config.entrypoint.CustomAuthenticationEntryPoint;
import com.palette.back_end.config.filter.JwtTokenFilter;
import com.palette.back_end.repository.UserRepository;
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

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults())
            .authorizeHttpRequests(au -> au
                    .anyRequest().permitAll())
            .exceptionHandling(ea -> ea
                    .accessDeniedHandler(new CustomAccessDeniedEntryPoint())
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
            .addFilterBefore(new JwtTokenFilter(key, userRepository), UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public ApplicationListener<AuthenticationSuccessEvent> successLogger() {
    return event -> log.info("success {}", event.getAuthentication());
  }
}
