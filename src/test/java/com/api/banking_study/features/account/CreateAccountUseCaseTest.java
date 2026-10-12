package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private CreateAccountUseCase useCase;

    UserEntity userEntity;
    AccountEntity accountEntity;

    @BeforeEach
    void setUp(){
        userEntity = new UserEntity(
                UUID.randomUUID(),
                "Daniel Kayque",
                "123",
                "123123123123",
                "daanielkayque@gmail.com",
                "ADMIN");

        accountEntity = new AccountEntity(UUID.randomUUID(), userEntity);
    }

    @Test
    public void shouldCreateAccount(){
        Mockito.when(repository.save(Mockito.any(AccountEntity.class))).thenReturn(accountEntity);

        AccountResponseDto dto = useCase.execute(userEntity);

        Assertions.assertEquals(dto.username(), userEntity.getUsername());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUserIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    public void shouldThrowAccountAlreadyExistsExceptionWhenAccountAlreadyExists(){
        Mockito.when(repository.existsByUserId(userEntity.getId())).thenReturn(true);
        Assertions.assertThrows(AccountAlreadyExistsException.class, () -> useCase.execute(userEntity));
    }
}
