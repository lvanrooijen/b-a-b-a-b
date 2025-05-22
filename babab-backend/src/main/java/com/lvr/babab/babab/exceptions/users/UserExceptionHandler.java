package com.lvr.babab.babab.exceptions.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleUserNotFoundException(UserNotFoundException e) {
    log.warn("[exception] type=[UserNotFoundException, message={}]", e.getMessage());
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    return ResponseEntity.status(detail.getStatus()).body(detail);
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ProblemDetail> handleDuplicateEmailException(DuplicateEmailException e) {
    log.warn("[exception] type=[DuplicateEmailException, message={}]", e.getMessage());
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    return ResponseEntity.status(detail.getStatus()).body(detail);
  }
}
