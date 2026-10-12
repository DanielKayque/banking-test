package com.api.banking_study.features.account;

import jakarta.validation.constraints.NotBlank;

public record AccountRequestDto (
        @NotBlank
        String document) {
}
