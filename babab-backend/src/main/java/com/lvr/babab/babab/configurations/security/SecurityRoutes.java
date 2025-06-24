package com.lvr.babab.babab.configurations.security;

public class SecurityRoutes {
  private static final String LOGIN = "/api/v1/login";
  private static final String REGISTER_USER = "/api/v1/register";
  private static final String REGISTER_BUSINESS_ACCOUNT = "/api/v1/register-business-account";
  private static final String RESET_PASSWORD = "/api/v1/password-reset/*";

  public static String[] getOpenPostPaths() {
    return new String[] {LOGIN, REGISTER_USER, REGISTER_BUSINESS_ACCOUNT, RESET_PASSWORD};
  }
}
