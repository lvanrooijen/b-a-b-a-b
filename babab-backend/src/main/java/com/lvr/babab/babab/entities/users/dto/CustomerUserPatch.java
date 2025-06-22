package com.lvr.babab.babab.entities.users.dto;

import java.time.LocalDate;

public record CustomerUserPatch(
    String email, String firstname, String lastname, LocalDate birthday) {}
