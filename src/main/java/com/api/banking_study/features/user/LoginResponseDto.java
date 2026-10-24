package com.api.banking_study.features.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(description = "JWT de curta duração para acessar endpoints protegidos") String accessToken,
        @Schema(description = "Token opaco para renovar a sessão") String refreshToken,
        @Schema(description = "Tipo do access token", example = "Bearer") String tokenType,
        @Schema(description = "Validade do access token em segundos", example = "900") long expiresIn,
        @Schema(description = "Validade do refresh token em segundos", example = "2592000") long refreshExpiresIn) {
}
