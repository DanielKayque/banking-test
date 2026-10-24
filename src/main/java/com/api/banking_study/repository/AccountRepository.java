package com.api.banking_study.repository;

import com.api.banking_study.entities.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    boolean existsByUserId(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findByUserId(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AccountEntity a WHERE a.user.document = :document")
    Optional<AccountEntity> findByUser_Document(@Param("document") String document);
}
