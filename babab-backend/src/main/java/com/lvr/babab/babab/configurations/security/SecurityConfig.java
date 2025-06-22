package com.lvr.babab.babab.configurations.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;
  private final CorsConfig corsConfig;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter, CorsConfig corsConfig) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.corsConfig = corsConfig;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfig.corsConfiguration()))
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers(
                        "/api/v1/login", "/api/v1/register", "/api/v1/password-reset/*")
                    .permitAll()
                    .requestMatchers(HttpMethod.PATCH, "/api/v1/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/**")
                    .authenticated()
                    .anyRequest()
                    .authenticated());

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling(
        configures -> configures.authenticationEntryPoint(new NotAuthorizedEntryPoint()));

    return http.build();
  }
}
