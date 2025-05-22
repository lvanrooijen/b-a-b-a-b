package com.lvr.babab.babab.configurations.security;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptionConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecretKey secretKey() {
    return Jwts.SIG.HS512.key().build();
  }
}
