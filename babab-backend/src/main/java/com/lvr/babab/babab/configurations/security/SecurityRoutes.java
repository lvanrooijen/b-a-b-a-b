package com.lvr.babab.babab.configurations.security;

public class SecurityRoutes {
  private static final String BASE = "/api/v1";
  private static final String LOGIN = BASE + "/login";
  private static final String REGISTER_USER = BASE + "/register";
  private static final String REGISTER_BUSINESS_ACCOUNT = BASE + "/register-business-account";
  private static final String REQUEST_PASSWORD_RESET = BASE + "/password-reset/*";
  private static final String RESET_PASSWORD = BASE + "/password-new/*";

  public static String[] getOpenPostPaths() {
    return new String[] {
      LOGIN, REGISTER_USER, REGISTER_BUSINESS_ACCOUNT, REQUEST_PASSWORD_RESET, RESET_PASSWORD
    };
  }
}
