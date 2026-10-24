package com.api.banking_study.features.user;

import com.api.banking_study.entities.RefreshTokenEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private static final long REFRESH_EXPIRATION_SECONDS = 3600;

    @Mock
    private RefreshTokenRepository repository;

    private RefreshTokenService service;
    private UserEntity user;

    @BeforeEach
    void setUp() throws Exception {
        service = new RefreshTokenService(repository);
        setField(service, "refreshTokenExpirationSeconds", REFRESH_EXPIRATION_SECONDS);
        user = new UserEntity(UUID.randomUUID(), "Daniel", "password-hash", "12345678901", "daniel@example.com", "USER");
    }

    @Test
    void issuePersistsOnlyHashAndReturnsOpaqueToken() throws Exception {
        Instant beforeIssue = Instant.now();

        IssuedRefreshToken issued = service.issue(user);

        ArgumentCaptor<RefreshTokenEntity> captor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        verify(repository).save(captor.capture());
        RefreshTokenEntity persisted = captor.getValue();

        assertEquals(43, issued.value().length());
        assertNotEquals(issued.value(), persisted.getTokenHash());
        assertEquals(sha256(issued.value()), persisted.getTokenHash());
        assertEquals(user, persisted.getUser());
        assertNotNull(persisted.getFamilyId());
        assertTrue(persisted.getCreatedAt().compareTo(beforeIssue) >= 0);
        assertEquals(REFRESH_EXPIRATION_SECONDS,
                persisted.getExpiresAt().getEpochSecond() - persisted.getCreatedAt().getEpochSecond());
        assertEquals(persisted.getExpiresAt(), issued.expiresAt());
    }

    @Test
    void rotateRevokesCurrentTokenAndStoresSuccessorInSameFamily() throws Exception {
        UUID familyId = UUID.randomUUID();
        String oldRawToken = "old-refresh-token";
        Instant familyExpiresAt = Instant.now().plusSeconds(1200);
        RefreshTokenEntity current = new RefreshTokenEntity(
                UUID.randomUUID(), user, familyId, sha256(oldRawToken), Instant.now(), familyExpiresAt);
        when(repository.findByTokenHashForUpdate(anyString())).thenReturn(Optional.of(current));

        RefreshTokenService.RotatedRefreshToken result = service.rotate(oldRawToken);

        ArgumentCaptor<RefreshTokenEntity> captor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        verify(repository, org.mockito.Mockito.times(2)).save(captor.capture());
        RefreshTokenEntity successor = captor.getAllValues().stream()
                .filter(token -> !token.getId().equals(current.getId()))
                .findFirst()
                .orElseThrow();

        assertTrue(current.isRevoked());
        assertEquals(successor.getId(), current.getReplacedByTokenId());
        assertEquals(familyId, successor.getFamilyId());
        assertEquals(user, successor.getUser());
        assertEquals(familyExpiresAt, successor.getExpiresAt());
        assertNotEquals(oldRawToken, result.refreshToken().value());
        assertEquals(sha256(result.refreshToken().value()), successor.getTokenHash());
        assertEquals(user.getEmail(), result.userEmail());
        verify(repository).findByTokenHashForUpdate(sha256(oldRawToken));
    }

    @Test
    void rotateRejectsUnknownToken() {
        when(repository.findByTokenHashForUpdate(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidRefreshTokenException.class, () -> service.rotate("unknown-token"));

        verify(repository, never()).save(any(RefreshTokenEntity.class));
    }

    @Test
    void rotateRejectsExpiredTokenAndMarksItRevoked() throws Exception {
        RefreshTokenEntity expired = new RefreshTokenEntity(
                UUID.randomUUID(), user, UUID.randomUUID(), sha256("expired-token"),
                Instant.now().minusSeconds(7200), Instant.now().minusSeconds(3600));
        when(repository.findByTokenHashForUpdate(anyString())).thenReturn(Optional.of(expired));

        assertThrows(InvalidRefreshTokenException.class, () -> service.rotate("expired-token"));

        assertTrue(expired.isRevoked());
        verify(repository).save(expired);
    }

    @Test
    void reusingRevokedTokenRevokesRemainingActiveTokensInItsFamily() throws Exception {
        UUID familyId = UUID.randomUUID();
        RefreshTokenEntity reused = new RefreshTokenEntity(
                UUID.randomUUID(), user, familyId, sha256("reused-token"), Instant.now().minusSeconds(10), Instant.now().plusSeconds(1000));
        reused.revoke(Instant.now().minusSeconds(1), UUID.randomUUID());
        when(repository.findByTokenHashForUpdate(anyString())).thenReturn(Optional.of(reused));

        assertThrows(InvalidRefreshTokenException.class, () -> service.rotate("reused-token"));

        verify(repository).revokeActiveByFamilyId(org.mockito.ArgumentMatchers.eq(familyId), any(Instant.class));
        verify(repository, never()).save(any(RefreshTokenEntity.class));
    }

    @Test
    void logoutRevokesKnownTokenAndIgnoresUnknownToken() throws Exception {
        RefreshTokenEntity active = new RefreshTokenEntity(
                UUID.randomUUID(), user, UUID.randomUUID(), sha256("logout-token"), Instant.now(), Instant.now().plusSeconds(1000));
        when(repository.findByTokenHashForUpdate(sha256("logout-token"))).thenReturn(Optional.of(active));
        when(repository.findByTokenHashForUpdate(sha256("missing-token"))).thenReturn(Optional.empty());

        service.revoke("logout-token");
        service.revoke("missing-token");

        assertTrue(active.isRevoked());
        verify(repository, never()).revokeActiveByFamilyId(any(UUID.class), any(Instant.class));
    }

    private static String sha256(String value) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(digest);
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
