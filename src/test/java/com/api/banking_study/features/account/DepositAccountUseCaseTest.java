package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DepositAccountUseCaseTest {

    @InjectMocks
    private DepositAccountUseCase useCase;

    @Mock
    private AccountRepository repository;

    @Mock
    private TransactionRepository transactionRepository;

    UUID userIdAlreadyExists;
    UUID accountIdAlreadyExists;
    BigDecimal amount;
    String description;
    UserEntity userEntity;
    AccountEntity accountEntity;
    UUID userIdNotExists;

    @BeforeEach
    void setUp(){
        userIdAlreadyExists = UUID.randomUUID();
        userIdNotExists = UUID.randomUUID();
        accountIdAlreadyExists = UUID.randomUUID();
        amount = new BigDecimal("100.00");
        description = "Depósito de teste";

        userEntity = new UserEntity(userIdAlreadyExists, "daniel", "123", "123123123123", "", "ADMIN");
        accountEntity = new AccountEntity(accountIdAlreadyExists, userEntity);
    }


    @Test
    public void shouldDepositWithSuccessfully(){
        Mockito.when(repository.findByUserId(userIdAlreadyExists)).thenReturn(Optional.of(accountEntity));

        BigDecimal value = useCase.execute(userIdAlreadyExists, amount, description);

        Assertions.assertEquals(amount, value);
        Mockito.verify(repository, Mockito.times(1)).findByUserId(userIdAlreadyExists);
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(TransactionEntity.class));
    }

    @Test
    public void shouldThrowAccountNotFoundExceptionWhenAccountNotFound(){
        Mockito.when(repository.findByUserId(userIdNotExists)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> useCase.execute(userIdNotExists, amount, description));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));
    }

    @Test
    public void shouldSaveTransactionWithDescription(){
        Mockito.when(repository.findByUserId(userIdAlreadyExists)).thenReturn(Optional.of(accountEntity));

        useCase.execute(userIdAlreadyExists, amount, "");

        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.argThat(t -> t.getDescription().equals("Operação depósito realizada")));
    }
}
