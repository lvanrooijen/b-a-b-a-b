package com.lvr.babab.babab.exceptions.authentication;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
