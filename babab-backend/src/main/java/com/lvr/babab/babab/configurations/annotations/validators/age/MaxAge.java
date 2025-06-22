package com.lvr.babab.babab.configurations.annotations.validators.age;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxAgeValidator.class)
public @interface MaxAge {
  String message() default "Max age of {value} is exceeded";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  int value();
}
