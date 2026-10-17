package com.api.banking_study.repository;

import com.api.banking_study.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    Page<TransactionEntity> findAllByAccountId(UUID accountId, Pageable pageable);
}
