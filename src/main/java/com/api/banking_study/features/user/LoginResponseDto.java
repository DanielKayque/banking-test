package com.api.banking_study.features.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record LoginResponseDto(
        @Schema(description = "Token JWT de acesso") String token,
        @Schema(description = "Tipo do token", example = "Bearer") String type,
        @Schema(description = "Data e hora de expiração do token", example = "2026-10-12T22:00:00Z") Instant expiresIn) {
}
