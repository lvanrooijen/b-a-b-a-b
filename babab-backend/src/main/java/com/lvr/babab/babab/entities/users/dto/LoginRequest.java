package com.lvr.babab.babab.entities.users.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String email, @NotBlank String password) {}
