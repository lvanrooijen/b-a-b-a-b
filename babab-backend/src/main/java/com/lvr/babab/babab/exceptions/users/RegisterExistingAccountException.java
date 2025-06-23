package com.lvr.babab.babab.exceptions.users;

public class RegisterExistingAccountException extends RuntimeException {
  String details;

  public RegisterExistingAccountException(String message, String details) {
    super(message);
    this.details = details;
  }
}
