package com.lvr.babab.babab.configurations.annotations.validators.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
  String message() default "Password does not meet the requirements";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
