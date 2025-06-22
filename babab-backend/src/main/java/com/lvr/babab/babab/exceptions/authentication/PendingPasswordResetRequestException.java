package com.lvr.babab.babab.exceptions.authentication;

import java.time.Duration;
import lombok.Getter;

@Getter
public class PendingPasswordResetRequestException extends RuntimeException {
  Duration duration;

  public PendingPasswordResetRequestException(String message, Duration duration) {
    super(message);
  }
}
