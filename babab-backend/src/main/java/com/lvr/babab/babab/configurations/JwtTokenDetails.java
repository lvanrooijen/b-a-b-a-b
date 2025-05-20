package com.lvr.babab.babab.configurations;

import java.util.Date;

public record JwtTokenDetails(String email, String[] roles, Date issuedAt, Date expiresAt) {
  public boolean isExpired() {
    return expiresAt.before(new Date());
  }
}
