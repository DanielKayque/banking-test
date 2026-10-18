package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.features.transactions.TransactionType;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class DepositAccountUseCase {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public BigDecimal execute(UUID userId, BigDecimal amount, String description){

        Long amountCents = amount.movePointRight(2).longValueExact();

        AccountEntity entity = repository.findByUserId(userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        entity.deposit(amountCents);

        transactionRepository.save(new TransactionEntity(UUID.randomUUID(),
                entity,
                description != null && !description.isBlank() ? description : "Operação depósito realizada",
                amountCents,
                TransactionType.DEPOSIT,
                Instant.now()));

        return BigDecimal.valueOf(entity.getAmountCent(), 2);
    }
}
