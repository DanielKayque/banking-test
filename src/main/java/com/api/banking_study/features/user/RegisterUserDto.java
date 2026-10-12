package com.api.banking_study.features.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
        @NotNull
        @Size(min = 3, max = 255)
        @Schema(description = "Nome de exibição do usuário", example = "Maria Silva", minLength = 3, maxLength = 255)
        String username,

        @NotNull
        @Email
        @Schema(description = "E-mail usado para autenticação", example = "maria.silva@example.com")
        String email,

        @NotNull
        @Schema(description = "Senha do usuário", example = "Senha@123", minLength = 3, maxLength = 255, format = "password")
        String password,

        @NotNull
        @Size(min = 11, max = 11)
        @Schema(description = "Documento do usuário, com 11 caracteres", example = "12345678901", minLength = 11, maxLength = 11)
        String document
) {
}
