package com.api.banking_study.features.user;

import java.time.Instant;

public record IssuedRefreshToken(String value, Instant expiresAt) {
}
