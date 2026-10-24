package com.api.banking_study.features.user;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
public class LoginUseCase {
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public LoginUseCase(TokenService tokenService, RefreshTokenService refreshTokenService,
                        AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public LoginResponseDto execute(LoginUserDto data) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.email(), data.password()));
        UserEntity user = (UserEntity) authentication.getPrincipal();

        Instant accessExpiresAt = tokenService.getAccessTokenExpirationDate();
        IssuedRefreshToken refreshToken = refreshTokenService.issue(user);

        return new LoginResponseDto(
                tokenService.generateAccessToken(user),
                refreshToken.value(),
                "Bearer",
                Duration.between(Instant.now(), accessExpiresAt).toSeconds(),
                Duration.between(Instant.now(), refreshToken.expiresAt()).toSeconds());
    }
}
