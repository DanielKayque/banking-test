package com.api.banking_study.features.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotNull
        @Size(min = 3, max = 255)
        String username,

        @NotNull
        @Email
        String email,

        @NotNull
        String password,

        @NotNull
        @Size(min = 11, max = 11)
        String document
) {
}
