package com.lvr.babab.babab.configurations.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class NotAuthorizedEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    log.warn("Unauthorized request to: {} — Reason: {}", request.getRequestURI(), authException.getMessage());

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("{\"error\": \"Unauthorized\"}");
    response.getWriter().flush();
  }
}
