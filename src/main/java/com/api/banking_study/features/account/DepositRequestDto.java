package com.api.banking_study.features.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequestDto(
        @NotNull
        @Positive
        BigDecimal amount) {
}
