package com.lvr.babab.babab.configurations.security;

import com.lvr.babab.babab.entities.users.User;
import com.lvr.babab.babab.entities.users.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  private static final String AUTHORIZATION_HEADER_JWT_PREFIX = "Bearer ";
  private final UserService userService;
  private final JwtService jwtService;

  public JwtAuthFilter(UserService userService, JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  private static boolean requestHasValidAuthHeader(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
    return authHeader != null && authHeader.startsWith(AUTHORIZATION_HEADER_JWT_PREFIX);
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
    }

    if (requestHasValidAuthHeader(request)) {
      getUserFromAuthorizationHeader(request.getHeader(AUTHORIZATION_HEADER_NAME))
          .ifPresent(
              principal -> {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
              });
    }
    log.info("=== JwtAuthFilter: PATH = {}", request.getRequestURI());
    log.info("=== Authorization header = {}", request.getHeader("Authorization"));
    filterChain.doFilter(request, response);
  }

  private Optional<User> getUserFromAuthorizationHeader(String authorization) {
    return jwtService
        .readToken(authorization.substring(AUTHORIZATION_HEADER_JWT_PREFIX.length()))
        .filter(token -> !token.isExpired())
        .flatMap(
            token -> Optional.ofNullable((User) userService.loadUserByUsername(token.email())));
  }
}
