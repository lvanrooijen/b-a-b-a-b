package com.lvr.babab.babab.configurations.annotations.validators.sqlinjections;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SqlSafeInputValidator implements ConstraintValidator<SqlSafeInput, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return false;
  }
}

// TODO
// Karakters:   '    "   ;   --   /* ... */
// Keywords (case-insensitive): DROP, SELECT, DELETE, INSERT, UPDATE, EXEC, UNION, XP_
