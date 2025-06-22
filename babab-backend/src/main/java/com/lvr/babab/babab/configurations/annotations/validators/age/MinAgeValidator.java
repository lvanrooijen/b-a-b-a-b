package com.lvr.babab.babab.configurations.annotations.validators.age;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {
    private int age;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.age = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if(birthdate == null){
            return true; // geen side effects toch
        }
        return birthdate.plusYears(age).isBefore(LocalDate.now()) ||
                birthdate.plusYears(age).isEqual(LocalDate.now());
    }
}
