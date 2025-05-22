package com.lvr.babab.babab.entities.users.dto;

import java.time.LocalDate;

public record PatchUser(String email, String firstname, String lastname, LocalDate birthday) {}
