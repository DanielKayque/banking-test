package com.api.banking_study.features.user;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterUserUseCase {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO execute(RegisterUserDto dto) {
        if (repository.findByEmail(dto.email()) != null){
            throw new UserAlreadyRegisteredException("email");
        }

        if (repository.existsByDocument(dto.document())) {
            throw new UserAlreadyRegisteredException("document");
        }

        String passwordHash = passwordEncoder.encode(dto.password());

        UserEntity entity = new UserEntity(
                UUID.randomUUID(), dto.username(),
                passwordHash,
                dto.document(),
                dto.email(),
            "USER");

        UserEntity result = repository.save(entity);

        return new UserResponseDTO(
                result.getUsername(), result.getEmail(), result.getDocument(), result.getRole()
        );
    }

}
