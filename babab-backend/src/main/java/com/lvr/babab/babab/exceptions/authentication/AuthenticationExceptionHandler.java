package com.lvr.babab.babab.exceptions.authentication;

import static com.lvr.babab.babab.exceptions.ProblemTypes.*;

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
    problemDetail.setProperty("errors", errors);
    problemDetail.setType(VALIDATION_ERROR);
    return problemDetail;
  }

  @ExceptionHandler(FailedSendPasswordResetEmailException.class)
  public ProblemDetail handleFailedSendPasswordResetEmailException(
      FailedSendPasswordResetEmailException e) {
    log.warn("[exception] type=FailedSendPasswordResetEmailException, message={}", e.getMessage());

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Something went wrong try again later");
    problemDetail.setType(FAILED_PASSWORD_RESET_EMAIL_REQUEST);
    return problemDetail;
  }

  @ExceptionHandler(PendingPasswordResetRequestException.class)
  public ProblemDetail handlePendingPasswordResetRequestException(
      PendingPasswordResetRequestException e) {
    log.warn("[exception] type=PendingPasswordResetRequestException, message={}", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Wait " + e.duration + " minutes");
    problemDetail.setType(PENDING_PASSWORD_RESET_REQUEST);
    return problemDetail;
  }

  @ExceptionHandler(ForbiddenException.class)
  public ProblemDetail handleForbiddenException(ForbiddenException e) {
    log.warn("[exception] type=ForbiddenException, message={}", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    problemDetail.setType(FORBIDDEN);
    return problemDetail;
  }

  @ExceptionHandler(FailedLoginException.class)
  public ProblemDetail handleFailedLoginException(FailedLoginException e) {
    log.warn("[exception] type=FailedLoginException, message={}", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "username or password is incorrect");
    problemDetail.setType(FAILED_LOGIN);
    return problemDetail;
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ProblemDetail handlePasswordMismatchException(PasswordMismatchException e) {
    log.warn("[exception] type=[PasswordMismatchException, message={}]", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Password is incorrect");
    problemDetail.setType(PASSWORD_MISMATCH);
    return problemDetail;
  }

  @ExceptionHandler(AdminOnlyActionException.class)
  public ProblemDetail handleAdminOnlyException(AdminOnlyActionException e) {
    log.warn("[exception] type=[AdminOnlyException, message={}]", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Admin only action");
    problemDetail.setType(ADMIN_ONLY);
    return problemDetail;
  }

  @ExceptionHandler(PasswordRequestNotFound.class)
  public ProblemDetail handlePasswordRequestNotFound(PasswordRequestNotFound e) {
    log.warn("[exception] type=[PasswordRequestNotFoundException, message={}]", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "Invalid token, request a new password reset");
    problemDetail.setType(PASSWORD_RESET_REQUEST_NOT_FOUND);
    return problemDetail;
  }
}
