package com.api.banking_study.features.transactions;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.features.account.AccountNotFoundException;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ListTransactionsUseCaseTest {

    @InjectMocks
    private ListTransactionsUseCase useCase;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private UUID userId;
    private AccountEntity account;
    private TransactionEntity transaction;

    @BeforeEach
    void setUp(){
        userId = UUID.randomUUID();

        UserEntity user = new UserEntity(
                userId,
                "Daniel",
                "password",
                "12312312312",
                "daniel@email.com",
                "USER"
        );

        account = new AccountEntity(UUID.randomUUID(), user);

        transaction = new TransactionEntity(
                UUID.randomUUID(),
                account,
                "Depósito",
                6450L,
                TransactionType.DEPOSIT,
                Instant.parse("2026-10-17T10:00:00Z")
        );
    }

    @Test
    void shouldReturnListOfTransactionsOrderedByCreationDateDescending() {
        Page<TransactionEntity> transactionPage = new PageImpl<>(
                List.of(transaction),
                Pageable.unpaged(),
                1
        );

        Mockito.when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.of(account));
        Mockito.when(transactionRepository.findAllByAccountId(
                        Mockito.eq(account.getId()),
                        Mockito.any(Pageable.class)
                ))
                .thenReturn(transactionPage);

        Page<ListTransactionsResponseDto> result =
                useCase.execute(0, 10, userId);

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("Depósito", result.getContent().get(0).description());
        Assertions.assertEquals(new BigDecimal("64.50"), result.getContent().get(0).amount());
        Assertions.assertEquals(TransactionType.DEPOSIT.toString(), result.getContent().get(0).type());

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        Mockito.verify(transactionRepository).findAllByAccountId(
                Mockito.eq(account.getId()),
                pageableCaptor.capture()
        );

        Sort.Order order = pageableCaptor.getValue()
                .getSort()
                .getOrderFor("createdAt");

        Assertions.assertNotNull(order);
        Assertions.assertEquals(Sort.Direction.DESC, order.getDirection());
        Assertions.assertEquals(0, pageableCaptor.getValue().getPageNumber());
        Assertions.assertEquals(10, pageableCaptor.getValue().getPageSize());
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenAccountDoesNotExist() {
        Mockito.when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                AccountNotFoundException.class,
                () -> useCase.execute(0, 10, userId)
        );

        Mockito.verifyNoInteractions(transactionRepository);
    }

}
