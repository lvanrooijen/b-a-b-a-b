package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.entities.users.User;

public record loginResponse(Long id, String email) {
  public static loginResponse to(User user) {
    return new loginResponse(user.getId(), user.getEmail());
  }
}
