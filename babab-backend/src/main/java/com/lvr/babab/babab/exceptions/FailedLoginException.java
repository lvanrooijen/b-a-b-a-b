package com.lvr.babab.babab.exceptions.authentication;

public class FailedLoginException extends RuntimeException {
  public FailedLoginException(String message) {
    super(message);
  }
}
