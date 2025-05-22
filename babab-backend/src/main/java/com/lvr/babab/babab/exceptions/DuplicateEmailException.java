package com.lvr.babab.babab.exceptions.authentication;

public class DuplicateEmailException extends RuntimeException {
  public DuplicateEmailException(String message) {
    super(message);
  }
}
