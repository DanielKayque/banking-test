package com.api.banking_study.features.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginUserDto(
        @Email
        String email,
        @Size(min = 6, max = 255)
        String password) {
}
