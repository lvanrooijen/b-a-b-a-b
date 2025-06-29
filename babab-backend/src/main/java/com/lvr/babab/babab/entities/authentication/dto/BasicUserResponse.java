package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.entities.users.BusinessUser;
import com.lvr.babab.babab.entities.users.CustomerUser;

public record BasicUserResponse(Long id, String email) {
  public static BasicUserResponse to(CustomerUser user) {
    return new BasicUserResponse(user.getId(), user.getEmail());
  }

  public static BasicUserResponse to(BusinessUser user) {
    return new BasicUserResponse(user.getId(), user.getEmail());
  }
}
