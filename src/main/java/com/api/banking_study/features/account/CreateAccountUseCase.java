package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.features.user.UserNotFoundException;
import com.api.banking_study.repository.AccountRepository;
import com.api.banking_study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateAccountUseCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto execute(String document){
        if (document == null) throw new IllegalArgumentException("Invalid document");

        UserEntity user = userRepository.findByDocument(document).orElseThrow(() -> new UserNotFoundException("User not found."));

        if (accountRepository.existsByUserId(user.getId())){
            throw new AccountAlreadyExistsException("Account already exists for this user " + user.getUsername());
        };

        AccountEntity entity = accountRepository.save(new AccountEntity(UUID.randomUUID(), user));

        return new AccountResponseDto(entity.getUser().getUsername(), entity.getUser().getEmail(), BigDecimal.valueOf(entity.getAmountCent(), 2));
    }
}
