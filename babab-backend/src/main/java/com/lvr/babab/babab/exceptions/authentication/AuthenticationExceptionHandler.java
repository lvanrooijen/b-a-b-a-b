package com.lvr.babab.babab.exceptions.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler {
  @ExceptionHandler(FailedSendPasswordResetEmailException.class)
  public ResponseEntity<ProblemDetail> handleFailedSendPasswordResetEmailException(
      FailedSendPasswordResetEmailException e) {
    log.warn("[exception] type=FailedSendPasswordResetEmailException, message={}", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Something went wrong try again later");
    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PendingPasswordResetRequestException.class)
  public ResponseEntity<ProblemDetail> handlePendingPasswordResetRequestException(
      PendingPasswordResetRequestException e) {
    log.warn("[exception] type=PendingPasswordResetRequestException, message={}", e.getMessage());
    ProblemDetail detail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Wait " + e.duration + " minutes");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ProblemDetail> handleForbiddenException(ForbiddenException e) {
    log.warn("[exception] type=ForbiddenException, message={}", e.getMessage());
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(detail);
  }

  @ExceptionHandler(FailedLoginException.class)
  public ResponseEntity<ProblemDetail> handleFailedLoginException(FailedLoginException e) {
    log.warn("[exception] type=FailedLoginException, message={}", e.getMessage());
    ProblemDetail detail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "username or password is incorrect");
    return ResponseEntity.status(detail.getStatus()).body(detail);
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<ProblemDetail> handlePasswordMismatchException(
      PasswordMismatchException e) {
    log.warn("[exception] type=[PasswordMismatchException, message={}]", e.getMessage());
    ProblemDetail detail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Password is incorrect");
    return ResponseEntity.status(detail.getStatus()).body(detail);
  }

  @ExceptionHandler(AdminOnlyActionException.class)
  public ResponseEntity<ProblemDetail> handleAdminOnlyException(AdminOnlyActionException e) {
    log.warn("[exception] type=[AdminOnlyException, message={}]", e.getMessage());
    ProblemDetail detail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Admin only action");
    return ResponseEntity.status(detail.getStatus()).body(detail);
  }
}
