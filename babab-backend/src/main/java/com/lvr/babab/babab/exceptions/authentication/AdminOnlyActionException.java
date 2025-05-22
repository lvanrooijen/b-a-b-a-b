package com.lvr.babab.babab.exceptions.authentication;

public class AdminOnlyActionException extends RuntimeException {
  public AdminOnlyActionException(String message) {
    super(message);
  }
}
