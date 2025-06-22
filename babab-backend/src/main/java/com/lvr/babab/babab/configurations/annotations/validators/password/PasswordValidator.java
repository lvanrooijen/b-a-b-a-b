package com.lvr.babab.babab.configurations.annotations.validators.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    if (value.length() < 8 || value.length() > 30) {
      context
          .buildConstraintViolationWithTemplate("Password must be between 8 and 30 characters")
          .addConstraintViolation();
      return false;
    }

    if (!value.matches(".*[0-9].*")) {
      context
          .buildConstraintViolationWithTemplate("Password must contain at least 1 digit")
          .addConstraintViolation();
      return false;
    }

    if (!value.matches(".*[a-z].*")) {
      context
          .buildConstraintViolationWithTemplate("Password must contain at least 1 lowercase letter")
          .addConstraintViolation();
      return false;
    }

    if (!value.matches(".*[A-Z].*")) {
      context
          .buildConstraintViolationWithTemplate("Password must contain at least 1 uppercase letter")
          .addConstraintViolation();
      return false;
    }

    if (!value.matches(".*[\\W].*")) {
      context
          .buildConstraintViolationWithTemplate(
              "Password must contain at least 1 special character")
          .addConstraintViolation();
      return false;
    }

    if (value.contains(" ")) {
      context
          .buildConstraintViolationWithTemplate("Blank space is not allowed in password")
          .addConstraintViolation();
      return false;
    }

    context
        .buildConstraintViolationWithTemplate("Password does not meet requirements")
        .addConstraintViolation();
    return true;
  }
}
