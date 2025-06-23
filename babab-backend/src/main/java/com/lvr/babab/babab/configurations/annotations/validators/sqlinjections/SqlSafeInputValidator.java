package com.lvr.babab.babab.configurations.annotations.validators.sqlinjections;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlSafeInputValidator implements ConstraintValidator<SqlSafeInput, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    if (checkForUnsafeCharacters(value) || checkForUnsafeKeywords(value)) {
      context
          .buildConstraintViolationWithTemplate("Input is vulnerable for sql injections")
          .addConstraintViolation();
      return false;
    } else {
      return true;
    }
  }

  private boolean checkForUnsafeCharacters(String value) {
    return value.contains("\"")
        || value.contains(";")
        || value.contains("'")
        || value.contains("--");
  }

  private boolean checkForUnsafeKeywords(String value) {
    String[] keywords = {"DROP", "SELECT", "DELETE", "INSERT", "UPDATE", "EXEC", "UNION", "XP_"};
    boolean containsKeyword = false;
    for (String keyword : keywords) {
      if (value.toUpperCase().contains(keyword)) {
        containsKeyword = true;
        break;
      }
    }
    return containsKeyword;
  }
}
