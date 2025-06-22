package com.lvr.babab.babab.exceptions.authentication;

public class FailedSendPasswordResetEmailException extends RuntimeException {
  public FailedSendPasswordResetEmailException(String message) {
    super(message);
  }
}
