package com.lvr.babab.babab.exceptions.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFoundException(UserNotFoundException e) {
    log.warn("[exception] type=[UserNotFoundException, message={}]", e.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ProblemDetail handleDuplicateEmailException(DuplicateEmailException e) {
    log.warn("[exception] type=[DuplicateEmailException, message={}]", e.getMessage());
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "This email has already been registered");
  }

  @ExceptionHandler(RegisterExistingAccountException.class)
  public ProblemDetail handleRegisterExistingAccountException(RegisterExistingAccountException e) {
    log.warn("[exception] type=RegisterExistingAccountException, message={}", e.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.details);
  }
}
