package com.lvr.babab.babab.configurations.annotations.validators.age;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeValidator.class)
public @interface MinAge {
  String message() default "Minimal age of {value} is required";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int value();
}
