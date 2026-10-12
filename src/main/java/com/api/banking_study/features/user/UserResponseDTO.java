package com.api.banking_study.features.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDTO(
        @Schema(example = "Maria Silva") String username,
        @Schema(example = "maria.silva@example.com") String email,
        @Schema(example = "12345678901") String document,
        @Schema(example = "USER") String roles) {
}
