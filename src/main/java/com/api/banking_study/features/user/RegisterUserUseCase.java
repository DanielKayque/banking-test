package com.api.banking_study.features.user;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.UserRepository;
import com.api.banking_study.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterUserUseCase {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO execute(UserDto dto) {
        if (repository.findByEmail(dto.email()) != null){
            throw new RuntimeException("Email already registered");
        }

        String passwordHash = passwordEncoder.encode(dto.password());

        UserEntity entity = new UserEntity(
                UUID.randomUUID(), dto.username(),
                passwordHash,
                dto.document(),
                dto.email(),
            "USER");

        UserEntity result = repository.save(entity);

        UserResponseDTO response = new UserResponseDTO(
                result.getUsername(), result.getEmail(), result.getDocument(), result.getRole()
        );

        return response;
    }

}
