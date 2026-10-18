package com.api.banking_study.features.account;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountTransferResponseDto (
        @Schema(description = "Descrição da operação realizada", example = "Transferência para Maria Silva")
        String description,
        @Schema(description = "Valor da operação")
        BigDecimal amount,
        @Schema(description = "Horário da transação.")
        Instant date){
}
