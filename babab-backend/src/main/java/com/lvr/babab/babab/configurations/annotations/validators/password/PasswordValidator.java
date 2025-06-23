package com.lvr.babab.babab.configurations.annotations.validators.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }

    if (!hasValidLength(value)) {
      setErrorMessage(context, "Password length should be between 8 and 30 characters");
      return false;
    }

    if (!containsADigit(value)) {
      setErrorMessage(context, "Password must contain at least 1 digit");
      return false;
    }

    if (!containsLowerCase(value)) {
      setErrorMessage(context, "Password must contain at least 1 lowercase letter");
      return false;
    }

    if (!containsUpperCase(value)) {
      setErrorMessage(context, "Password must contain at least 1 uppercase letter");
      return false;
    }

    if (!containsSpecialCharacter(value)) {
      setErrorMessage(context, "Password must contain at least 1 special character");
      return false;
    }

    if (containsBlankSpace(value)) {
      setErrorMessage(context, "Password must contain at least 1 blank space");
      return false;
    }

    return true;
  }

  private void setErrorMessage(ConstraintValidatorContext context, String errorMessage) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
  }

  private boolean hasValidLength(String password) {
    return password.length() >= 8 && password.length() <= 30;
  }

  private boolean containsADigit(String password) {
    return password.matches(".*[0-9].*");
  }

  private boolean containsLowerCase(String password) {
    return password.matches(".*[a-z].*");
  }

  private boolean containsUpperCase(String password) {
    return password.matches(".*[A-Z].*");
  }

  private boolean containsSpecialCharacter(String password) {
    return password.matches(".*[\\W].*");
  }

  private boolean containsBlankSpace(String password) {
    return password.contains(" ");
  }
}
