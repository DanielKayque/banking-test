package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CheckBalanceUseCase {

    @Autowired
    private AccountRepository repository;

    @Transactional
    public CheckBalanceResponseDto execute(UUID userId) {
        AccountEntity account = repository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return new CheckBalanceResponseDto(BigDecimal.valueOf(account.getAmountCent(), 2));
    }
}
