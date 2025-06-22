package com.lvr.babab.babab.configurations.annotations.validators.sqlinjections;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SqlSafeInputValidator.class)
public @interface SqlSafeInput {
    String message() default "Input is not considered safe as it may contain a Sql injection";

    Class<?>[] groups() default {};

    Class<? extends Payload>[]  payload() default {};
}
