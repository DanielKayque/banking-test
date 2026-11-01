package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.DestinationAccountNotFoundException;
import com.api.banking_study.exceptions.InsufficientFundsException;
import com.api.banking_study.features.transactions.TransactionType;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AccountTransferUseCaseTest {

    @InjectMocks
    private AccountTransferUseCase useCase;

    @Mock
    private AccountRepository repository;

    @Mock
    private TransactionRepository transactionRepository;

    UserEntity user;

    AccountEntity accountOrigin;

    AccountTransferRequestDto dto;

    AccountEntity accountDestination;

    UserEntity userDestination;

    @BeforeEach
    void setUp(){
        user = UserEntity.builder()
                .id(UUID.randomUUID())
                .document("11111111111")
                .email("teste@email.com")
                .username("Daniel Kayque")
                .password("123")
                .createdAt(Instant.now())
                .build();

        userDestination = UserEntity.builder()
                .id(UUID.randomUUID())
                .document("22222222222")
                .email("teste@email.com")
                .username("Diogo Silva")
                .password("123")
                .createdAt(Instant.now())
                .build();

        accountOrigin = AccountEntity.builder()
                .id(UUID.randomUUID())
                .user(user)
                .amountCent(100L)
                .build();

        accountDestination = AccountEntity.builder()
                .id(UUID.randomUUID())
                .user(userDestination)
                .amountCent(0L)
                .build();

        dto = new AccountTransferRequestDto("22222222222", BigDecimal.valueOf(1));
    }

    @Test
    void shouldTransferAccountWithSuccessfully(){
        Mockito.when(repository.findByUserId(user.getId())).thenReturn(Optional.of(accountOrigin));
        Mockito.when(repository.findByUser_Document(Mockito.anyString())).thenReturn(Optional.of(accountDestination));

        AccountTransferResponseDto response = useCase.execute(user.getId(), dto);

        Mockito.verify(repository, Mockito.times(1)).findByUserId(user.getId());
        Mockito.verify(repository, Mockito.times(1)).findByUser_Document(dto.documentDestination());

        Assertions.assertEquals(0L, accountOrigin.getAmountCent());
        Assertions.assertEquals(100L, accountDestination.getAmountCent());

        Assertions.assertEquals("Transferência realizada para Diogo Silva", response.description());
        Assertions.assertEquals(new BigDecimal("1.00"), response.amount());
        Assertions.assertNotNull(response.date());

        Mockito.verify(transactionRepository, Mockito.times(2)).save(Mockito.any(TransactionEntity.class));
    }

    @Test
    void shouldThrowInsufficientFundsExceptionAndNotSaveTransactionsWhenBalanceIsTooLow(){
        Mockito.when(repository.findByUserId(user.getId())).thenReturn(Optional.of(accountOrigin));
        Mockito.when(repository.findByUser_Document(dto.documentDestination())).thenReturn(Optional.of(accountDestination));
        dto = new AccountTransferRequestDto(dto.documentDestination(), new BigDecimal("2.00"));

        Assertions.assertThrows(InsufficientFundsException.class,
                () -> useCase.execute(user.getId(), dto));

        Assertions.assertEquals(100L, accountOrigin.getAmountCent());
        Assertions.assertEquals(0L, accountDestination.getAmountCent());
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));
    }

    @Test
    void shouldThrowDestinationAccountNotFoundExceptionAndNotSaveTransactions(){
        Mockito.when(repository.findByUserId(user.getId())).thenReturn(Optional.of(accountOrigin));
        Mockito.when(repository.findByUser_Document(dto.documentDestination())).thenReturn(Optional.empty());

        Assertions.assertThrows(DestinationAccountNotFoundException.class,
                () -> useCase.execute(user.getId(), dto));

        Assertions.assertEquals(100L, accountOrigin.getAmountCent());
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));
    }

    @Test
    void shouldThrowAccountNotFoundExceptionWhenOriginAccountDoesNotExist(){
        Mockito.when(repository.findByUserId(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(user.getId(), dto));

        Mockito.verify(repository, Mockito.never()).findByUser_Document(Mockito.anyString());
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));
    }
}
