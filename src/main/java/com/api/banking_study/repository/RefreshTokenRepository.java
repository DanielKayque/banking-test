package com.api.banking_study.repository;

import com.api.banking_study.entities.RefreshTokenEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT token FROM RefreshTokenEntity token WHERE token.tokenHash = :tokenHash")
    Optional<RefreshTokenEntity> findByTokenHashForUpdate(@Param("tokenHash") String tokenHash);

    @Modifying
    @Query("UPDATE RefreshTokenEntity token SET token.revokedAt = :revokedAt WHERE token.familyId = :familyId AND token.revokedAt IS NULL")
    int revokeActiveByFamilyId(@Param("familyId") UUID familyId, @Param("revokedAt") Instant revokedAt);
}
