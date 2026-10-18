package com.api.banking_study.features.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountTransferRequestDto(
        @NotNull
        @Size(min = 11, max = 11)
        @Schema(description = "Documento da conta destino", example = "12345678900")
        String documentDestination,
        @NotNull
        @Positive(message = "Valor deve ser maior que zero")
        BigDecimal amount) {
}
