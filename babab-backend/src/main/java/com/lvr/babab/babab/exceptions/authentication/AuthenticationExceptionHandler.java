package com.lvr.babab.babab.exceptions.authentication;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleFailedValidation(MethodArgumentNotValidException e) {
    Map<String, String> errors =
        e.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage,
                    (existing, replacement) -> existing));
    String message = "Invalid Request Body";
    log.warn("[exception] type=MethodArgumentNotValidException, message={}", message);
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    problemDetail.setProperty("invalid-fields", errors);

    return problemDetail;
  }

  @ExceptionHandler(FailedSendPasswordResetEmailException.class)
  public ProblemDetail handleFailedSendPasswordResetEmailException(
      FailedSendPasswordResetEmailException e) {
    log.warn("[exception] type=FailedSendPasswordResetEmailException, message={}", e.getMessage());
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "Something went wrong try again later");
  }

  @ExceptionHandler(PendingPasswordResetRequestException.class)
  public ProblemDetail handlePendingPasswordResetRequestException(
      PendingPasswordResetRequestException e) {
    log.warn("[exception] type=PendingPasswordResetRequestException, message={}", e.getMessage());
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "Wait " + e.duration + " minutes");
  }

  @ExceptionHandler(ForbiddenException.class)
  public ProblemDetail handleForbiddenException(ForbiddenException e) {
    log.warn("[exception] type=ForbiddenException, message={}", e.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
  }

  @ExceptionHandler(FailedLoginException.class)
  public ProblemDetail handleFailedLoginException(FailedLoginException e) {
    log.warn("[exception] type=FailedLoginException, message={}", e.getMessage());
    return ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, "username or password is incorrect");
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ProblemDetail handlePasswordMismatchException(PasswordMismatchException e) {
    log.warn("[exception] type=[PasswordMismatchException, message={}]", e.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Password is incorrect");
  }

  @ExceptionHandler(AdminOnlyActionException.class)
  public ProblemDetail handleAdminOnlyException(AdminOnlyActionException e) {
    log.warn("[exception] type=[AdminOnlyException, message={}]", e.getMessage());
    return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Admin only action");
  }
}
