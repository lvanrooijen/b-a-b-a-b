package com.lvr.babab.babab.exceptions;

import java.net.URI;

public class ProblemTypes {
  private static final String HOST = "http://localhost:8080";
  private static final String BASE_ROUTE = HOST + "/api/v1/docs";
  private static final String ERRORS = BASE_ROUTE + "/errors";

  public static final URI VALIDATION_ERROR = URI.create(BASE_ROUTE + "/validation-error");
  public static final URI FAILED_PASSWORD_RESET_EMAIL_REQUEST =
      URI.create(BASE_ROUTE + "/failed-password-reset-email-request");
  public static final URI PENDING_PASSWORD_RESET_REQUEST =
      URI.create(BASE_ROUTE + "/pending-password-reset-request");
  public static final URI FORBIDDEN = URI.create(BASE_ROUTE + "/forbidden");
  public static final URI FAILED_LOGIN = URI.create(BASE_ROUTE + "/failed-login");
  public static final URI PASSWORD_MISMATCH = URI.create(BASE_ROUTE + "/password-mismatch");
  public static final URI ADMIN_ONLY = URI.create(BASE_ROUTE + "/admin-only");
  public static final URI PASSWORD_RESET_REQUEST_NOT_FOUND =
      URI.create(BASE_ROUTE + "/password-reset-request-not-found");
}
