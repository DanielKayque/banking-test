package com.api.banking_study.features.account;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record AccountResponseDto(
        @Schema(example = "Maria Silva") String username,
        @Schema(example = "maria.silva@example.com") String email,
        @Schema(description = "Saldo atual da conta", example = "100.00", minimum = "0") BigDecimal balance) {
}
