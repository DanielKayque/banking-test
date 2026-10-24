package com.api.banking_study.security;

import com.api.banking_study.entities.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenServiceTest {
    private static final String SECRET = "test-secret-used-only-by-token-service-tests";

    private TokenService tokenService;
    private UserEntity user;

    @BeforeEach
    void setUp() throws Exception {
        tokenService = new TokenService();
        setField(tokenService, "secret", SECRET);
        setField(tokenService, "accessTokenExpirationSeconds", 900L);
        user = new UserEntity(UUID.randomUUID(), "Daniel", "password-hash", "12345678901", "daniel@example.com", "USER");
    }

    @Test
    void generatedAccessTokenHasAccessPurposeAndCanBeValidated() {
        String accessToken = tokenService.generateAccessToken(user);

        assertEquals(user.getEmail(), tokenService.validateAccessToken(accessToken));
        assertEquals("access", JWT.decode(accessToken).getClaim("token_use").asString());
        assertNotNull(JWT.decode(accessToken).getIssuedAt());
        assertTrue(JWT.decode(accessToken).getExpiresAt().after(Date.from(Instant.now())));
    }

    @Test
    void tokenWithRefreshPurposeIsNotAcceptedAsAccessToken() {
        String refreshPurposeJwt = JWT.create()
                .withIssuer("auth-login")
                .withSubject(user.getEmail())
                .withClaim("token_use", "refresh")
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(Algorithm.HMAC256(SECRET));

        assertThrows(JWTVerificationException.class,
                () -> tokenService.validateAccessToken(refreshPurposeJwt));
    }

    @Test
    void opaqueRefreshTokenIsNotAcceptedAsAccessToken() {
        assertThrows(JWTVerificationException.class,
                () -> tokenService.validateAccessToken("opaque-refresh-token"));
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
