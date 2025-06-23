package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.configurations.annotations.validators.age.MaxAge;
import com.lvr.babab.babab.configurations.annotations.validators.age.MinAge;
import com.lvr.babab.babab.configurations.annotations.validators.password.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;

public record RegisterUserRequest(
    @Email @NotBlank String email,
    @NotBlank @Password String password,
    @NotBlank(message = "First name is required")
        @Length(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
        String firstname,
    @NotBlank(message = "Last name is required")
        @Length(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
        String lastname,
    @NotNull(message = "birthdate is required")
        @MinAge(value = 18, message = "Minimum age of 18 required")
        @MaxAge(value = 150, message = "Oke Vlad The Impaler, you're a +150 years old")
        LocalDate birthdate) {}
