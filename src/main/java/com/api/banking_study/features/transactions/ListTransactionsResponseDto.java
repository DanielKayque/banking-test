package com.api.banking_study.features.transactions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ListTransactionsResponseDto(
        UUID accountId,
        String description,
        String type,
        BigDecimal amount,
        Instant date
) {
}
