package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.configurations.annotations.validators.password.Password;
import jakarta.validation.constraints.Email;

public record UserPasswordResetRequest(
    @Email String email, @Password String password, @Password String passwordConfirmation) {}
