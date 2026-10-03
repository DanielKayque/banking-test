package com.api.banking_study.features.user;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.UserAlreadyRegisteredException;
import com.api.banking_study.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDto userDto;

    private UserEntity savedUser;

    @BeforeEach
    void setUp(){
        userDto = new UserDto("daniel", "daanielkayque@gmail.com", "123", "123123132123");

        savedUser = new UserEntity(UUID.randomUUID() ,"daniel", "daanielkayque@gmail.com", "123", "123123132123", "ADMIN");

    }

    @Test
    public void shouldThrowUserAlreadyRegisteredExceptionWhenUserAlreadyExists() {
        UserDetails user = Mockito.mock(UserDetails.class);

        when(repository.findByEmail(userDto.email())).thenReturn(user);

        Assertions.assertThrows(UserAlreadyRegisteredException.class, () -> useCase.execute(userDto));

        Mockito.verify(repository, Mockito.times(1)).findByEmail(userDto.email());
        Mockito.verify(repository, Mockito.never()).existsByDocument(Mockito.anyString());
    }

    @Test
    public void shouldReturnUserResponseDTO(){
        Mockito.when(repository.findByEmail(userDto.email())).thenReturn(null);
        Mockito.when(passwordEncoder.encode(userDto.password())).thenReturn("password");
        Mockito.when(repository.existsByDocument(userDto.document())).thenReturn(false);
        Mockito.when(repository.save(Mockito.any(UserEntity.class))).thenReturn(savedUser);

        UserResponseDTO result = useCase.execute(userDto);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
        Assertions.assertEquals(result.username(), userDto.username());
    }

    @Test
    void shouldThrowUserAlreadyRegisteredExceptionWhenDocumentIsAlreadyRegistered() {
        Mockito.when(repository.findByEmail(userDto.email())).thenReturn(null);
        Mockito.when(repository.existsByDocument(userDto.document())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyRegisteredException.class, () -> useCase.execute(userDto));

        Mockito.verify(repository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(repository, Mockito.times(1)).existsByDocument(userDto.document());
        Mockito.verify(passwordEncoder, Mockito.never()).encode(Mockito.anyString());
    }
}
