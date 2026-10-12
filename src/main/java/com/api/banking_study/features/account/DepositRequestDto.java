package com.api.banking_study.features.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequestDto(
        @NotNull
        @Positive
        @Schema(description = "Valor a ser depositado", example = "100.00")
        BigDecimal amount) {
}
