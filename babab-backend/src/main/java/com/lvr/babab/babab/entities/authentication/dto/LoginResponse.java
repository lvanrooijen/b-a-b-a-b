package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.entities.users.BusinessUser;
import com.lvr.babab.babab.entities.users.CustomerUser;

public record LoginResponse(Long id, String email) {
    public static LoginResponse to(CustomerUser user) {
    return new LoginResponse(user.getId(), user.getEmail());
  }

  public static LoginResponse to(BusinessUser user) {
    return new LoginResponse(user.getId(), user.getEmail());
  }
}
