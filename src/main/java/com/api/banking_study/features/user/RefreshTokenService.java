package com.api.banking_study.features.user;

import com.api.banking_study.entities.RefreshTokenEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${auth.jwt.refresh-token-expiration-seconds:2592000}")
    private long refreshTokenExpirationSeconds;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public IssuedRefreshToken issue(UserEntity user) {
        Instant now = Instant.now();
        String rawToken = generateRawToken();
        Instant expiresAt = now.plusSeconds(refreshTokenExpirationSeconds);

        repository.save(new RefreshTokenEntity(
                UUID.randomUUID(), user, UUID.randomUUID(), hash(rawToken), now, expiresAt));

        return new IssuedRefreshToken(rawToken, expiresAt);
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public RotatedRefreshToken rotate(String rawToken) {
        Instant now = Instant.now();
        RefreshTokenEntity current = repository.findByTokenHashForUpdate(hash(rawToken))
                .orElseThrow(InvalidRefreshTokenException::new);

        if (current.isRevoked()) {
            // Reuse of a rotated/revoked token invalidates the whole token family.
            repository.revokeActiveByFamilyId(current.getFamilyId(), now);
            throw new InvalidRefreshTokenException();
        }

        if (current.isExpiredAt(now)) {
            current.revoke(now, null);
            throw new InvalidRefreshTokenException();
        }

        String nextRawToken = generateRawToken();
        UUID nextId = UUID.randomUUID();
        // The token family has an absolute lifetime measured from login; rotation does not extend it.
        Instant nextExpiresAt = current.getExpiresAt();
        RefreshTokenEntity next = new RefreshTokenEntity(
                nextId, current.getUser(), current.getFamilyId(), hash(nextRawToken), now, nextExpiresAt);

        String userEmail = current.getUser().getEmail();
        current.revoke(now, nextId);
        repository.save(current);
        repository.save(next);

        return new RotatedRefreshToken(userEmail, new IssuedRefreshToken(nextRawToken, nextExpiresAt));
    }

    @Transactional
    public void revoke(String rawToken) {
        repository.findByTokenHashForUpdate(hash(rawToken))
                .ifPresent(token -> token.revoke(Instant.now(), null));
    }

    private String generateRawToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    public record RotatedRefreshToken(String userEmail, IssuedRefreshToken refreshToken) {
    }
}
