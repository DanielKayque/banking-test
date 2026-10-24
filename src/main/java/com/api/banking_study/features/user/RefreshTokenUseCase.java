package com.api.banking_study.features.user;

import com.api.banking_study.security.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
public class RefreshTokenUseCase {
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public RefreshTokenUseCase(RefreshTokenService refreshTokenService, TokenService tokenService) {
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public LoginResponseDto execute(String rawRefreshToken) {
        RefreshTokenService.RotatedRefreshToken rotated = refreshTokenService.rotate(rawRefreshToken);
        Instant accessExpiresAt = tokenService.getAccessTokenExpirationDate();
        IssuedRefreshToken nextRefreshToken = rotated.refreshToken();

        return new LoginResponseDto(
                tokenService.generateAccessToken(rotated.userEmail()),
                nextRefreshToken.value(),
                "Bearer",
                Duration.between(Instant.now(), accessExpiresAt).toSeconds(),
                Duration.between(Instant.now(), nextRefreshToken.expiresAt()).toSeconds());
    }
}
