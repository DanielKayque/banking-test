package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.features.user.UserNotFoundException;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateAccountUseCase useCase;

    String documentAlreadyExists;
    String documentNotExists;

    UserEntity userEntity;
    AccountEntity accountEntity;

    @BeforeEach
    void setUp(){
        documentAlreadyExists = "123123123123";

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
        Mockito.when(userRepository.findByDocument(documentAlreadyExists)).thenReturn(Optional.of(userEntity));
        Mockito.when(repository.save(Mockito.any(AccountEntity.class))).thenReturn(accountEntity);

        AccountResponseDto dto = useCase.execute(documentAlreadyExists);

        Mockito.verify(userRepository, Mockito.times(1)).findByDocument(Mockito.any(String.class));
        Assertions.assertEquals(dto.username(), userEntity.getUsername());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist(){
        Mockito.when(userRepository.findByDocument(documentNotExists)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> useCase.execute(documentNotExists));
    }

    @Test
    public void shouldThrowUserNotFoundWhenDocumentIsNull(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
    }

    @Test
    public void shouldThrowAccountAlreadyExistsExceptionWhenAccountAlreadyExists(){
        Mockito.when(repository.existsByUserId(userEntity.getId())).thenReturn(true);
        Mockito.when(userRepository.findByDocument(documentAlreadyExists)).thenReturn(Optional.of(userEntity));

        Assertions.assertThrows(AccountAlreadyExistsException.class, () -> useCase.execute(documentAlreadyExists));
    }
}
