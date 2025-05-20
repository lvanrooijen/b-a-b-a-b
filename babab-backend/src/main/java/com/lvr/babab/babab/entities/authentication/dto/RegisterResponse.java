package com.lvr.babab.babab.entities.authentication.dto;

import com.lvr.babab.babab.entities.users.dto.UserResponse;

public record RegisterResponse(String token, UserResponse user) {}
