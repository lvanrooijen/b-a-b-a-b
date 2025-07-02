package com.lvr.babab.babab.exceptions;

import static com.lvr.babab.babab.exceptions.ProblemTypes.INVALID_REQUEST_BODY;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global exception handler voor generic/globally available exceptions */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.warn("[exception] type=HttpMessageNotReadableException, message={}", e.getMessage());
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid or Missing request body");
    problemDetail.setType(INVALID_REQUEST_BODY);
    return problemDetail;
  }
}
