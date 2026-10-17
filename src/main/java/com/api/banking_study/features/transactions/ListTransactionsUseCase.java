package com.api.banking_study.features.transactions;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.features.account.AccountNotFoundException;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ListTransactionsUseCase {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Page<ListTransactionsResponseDto> execute(int page, int size,UUID userId){
        AccountEntity entity = accountRepository.findByUserId(userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        UUID accountId = entity.getId();

        PageRequest pageable = PageRequest.of(page, size);

        Page<TransactionEntity> transaction = transactionRepository.findAllByAccountId(accountId, pageable);

        return transaction.map((t) -> new ListTransactionsResponseDto(
                t.getAccount().getId(),
                t.getDescription(),
                t.getType().toString(),
                BigDecimal.valueOf(t.getAmountCent(), 2),
                t.getCreatedAt()));
    }
}
