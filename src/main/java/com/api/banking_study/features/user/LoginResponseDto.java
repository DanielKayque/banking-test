package com.api.banking_study.features.user;

import java.time.Instant;

public record LoginResponseDto(String token, String type, Instant expiresIn) {
}
