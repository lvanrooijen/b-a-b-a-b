package com.lvr.babab.babab.exceptions;

public class FailedLoginException extends RuntimeException {
  public FailedLoginException(String message) {
    super(message);
  }
}
