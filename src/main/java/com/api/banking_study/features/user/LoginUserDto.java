package com.api.banking_study.features.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginUserDto(
        @Email
        @Schema(description = "E-mail cadastrado", example = "maria.silva@example.com")
        String email,
        @Size(min = 3, max = 255)
        @Schema(description = "Senha do usuário", example = "Senha@123", minLength = 3, maxLength = 255, format = "password")
        String password) {
}
