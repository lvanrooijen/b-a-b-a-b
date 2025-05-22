package com.lvr.babab.babab.entities.users.dto;

import com.lvr.babab.babab.entities.users.User;
import java.time.LocalDate;

public record UserResponseMax(
    Long id, String email, String firstName, String lastName, LocalDate birthday) {
  public static UserResponseMax to(User user) {
    return new UserResponseMax(
        user.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getBirthdate());
  }
}
