package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.configurations.annotations.validators.password.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record RegisterBusinessAccountRequest(
    @NotBlank(message = "Company Name is required")
        @Length(min = 1, max = 250, message = "Company name must be between 1 and 250 characters")
        String companyName,
    @NotBlank(message = "KVK number is required")
        @Length(min = 8, max = 8, message = "Kvk number must be 8 characters")
        @Positive(message = "KvK number must be a number, and can only contain positive numbers")
        String kvkNumber,
    @Email(message = "Invalid email address") String email,
    @Password @NotBlank String password) {}
