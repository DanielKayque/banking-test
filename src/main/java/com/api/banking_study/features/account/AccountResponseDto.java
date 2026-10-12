package com.api.banking_study.features.account;

import java.math.BigDecimal;

public record AccountResponseDto(String username, String email, BigDecimal balance) {
}
