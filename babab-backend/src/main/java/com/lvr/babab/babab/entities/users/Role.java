package com.lvr.babab.babab.entities.users;

public enum Role {
  USER("ROLE_USER"),
  BUSINESS("ROLE_BUSINESS"),
  ADMIN("ROLE_ADMIN");

  private final String role;

  Role(String role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return role;
  }
}
