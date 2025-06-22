package com.lvr.babab.babab.configurations.annotations.validators.age;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MaxAgeValidator implements ConstraintValidator<MaxAge, LocalDate> {
  private int age;

  @Override
  public void initialize(MaxAge constraintAnnotation) {
    this.age = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
    if (birthdate == null) {
      return true;
    }
    return birthdate.plusYears(age).isAfter(LocalDate.now())
        || birthdate.plusYears(age).isEqual(LocalDate.now());
  }
}
