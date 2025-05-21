package com.lvr.babab.babab.exceptions.authentication;

public class PasswordMismatchException extends RuntimeException {
  public PasswordMismatchException(String message) {
    super(message);
  }
}
