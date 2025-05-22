package com.lvr.babab.babab.configurations.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
  private final String corsClient;

  public CorsConfig(@Value("${babab.cors}") String corsClient) {
    this.corsClient = corsClient;
  }

  @Bean
  CorsConfigurationSource corsConfiguration() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(corsClient));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(
        List.of("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With"));
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  FilterRegistrationBean<CorsFilter> corsFilterBean() {

    FilterRegistrationBean<CorsFilter> bean =
        new FilterRegistrationBean<>(new CorsFilter(corsConfiguration()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }
}
