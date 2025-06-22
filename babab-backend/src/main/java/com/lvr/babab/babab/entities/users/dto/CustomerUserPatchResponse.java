package com.lvr.babab.babab.entities.users.dto;

import com.lvr.babab.babab.entities.users.CustomerUser;
import java.time.LocalDate;

public record CustomerUserPatchResponse(
    Long id, String email, String firstName, String lastName, LocalDate birthday) {
  public static CustomerUserPatchResponse to(CustomerUser user) {
    return new CustomerUserPatchResponse(
        user.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getBirthdate());
  }
}
