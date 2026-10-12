package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DepositAccountUseCase {
    @Autowired
    private AccountRepository repository;

    @Transactional
    public BigDecimal execute(UUID userId, BigDecimal amount){

        Long amountCents = amount.movePointRight(2).longValueExact();

        AccountEntity entity = repository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        entity.deposit(amountCents);

        return BigDecimal.valueOf(entity.getAmountCent(), 2);
    }
}
