package com.api.banking_study.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponseDTO(
        @Schema(description = "Descrição do erro", example = "Invalid request data") String message,
        @Schema(description = "Código HTTP do erro", example = "400") int code) {
}
