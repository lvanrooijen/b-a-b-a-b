package com.lvr.babab.babab.entities.users.dto;

import com.lvr.babab.babab.entities.users.User;

public record UserResponseMin(Long id, String email) {
  public static UserResponseMin to(User user) {
    return new UserResponseMin(user.getId(), user.getEmail());
  }
}
