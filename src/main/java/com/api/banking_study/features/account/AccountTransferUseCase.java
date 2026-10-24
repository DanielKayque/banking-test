package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.features.transactions.TransactionType;
import com.api.banking_study.exceptions.DestinationAccountNotFoundException;
import com.api.banking_study.exceptions.InsufficientFundsException;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.TransactionRepository;
import com.api.banking_study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class AccountTransferUseCase {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional()
    public AccountTransferResponseDto execute(UUID userId, AccountTransferRequestDto dto){
        AccountEntity entityAccountOrigin = repository.findByUserId(userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        AccountEntity accountEntityDestination = repository.findByUser_Document(dto.documentDestination()).orElseThrow(() -> new DestinationAccountNotFoundException("Destination account not found"));

        Long amountCents = dto.amount().movePointRight(2).longValueExact();

        if (entityAccountOrigin.getAmountCent() < amountCents){
            throw new InsufficientFundsException("Insufficient funds");
        }

        entityAccountOrigin.withdraw(amountCents);

        TransactionEntity transactionEntityOrigin = new TransactionEntity(UUID.randomUUID(),
                entityAccountOrigin,
                "Transferência realizada para " + accountEntityDestination.getUser().getUsername(),
                amountCents,
                TransactionType.WITHDRAW,
                Instant.now()
        );

        transactionRepository.save(transactionEntityOrigin);

        accountEntityDestination.deposit(amountCents);

        TransactionEntity transactionEntityDestination = new TransactionEntity(UUID.randomUUID(),
                accountEntityDestination,
                "Depósito de: " + entityAccountOrigin.getUser().getUsername(),
                amountCents,
                TransactionType.DEPOSIT,
                Instant.now());

        transactionRepository.save(transactionEntityDestination);

        return new AccountTransferResponseDto(transactionEntityOrigin.getDescription(),
                BigDecimal.valueOf(amountCents, 2), transactionEntityOrigin.getCreatedAt());
    }
}
