package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.AuthenticatedUserRequiredException;
import com.api.banking_study.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateAccountUseCase {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto execute(UserEntity user){
        if (user == null) {
            throw new AuthenticatedUserRequiredException("Authenticated user is required");
        }

        if (accountRepository.existsByUserId(user.getId())){
            throw new AccountAlreadyExistsException("Account already exists for this user " + user.getUsername());
        };

        AccountEntity entity = accountRepository.save(new AccountEntity(UUID.randomUUID(), user));

        return new AccountResponseDto(entity.getUser().getUsername(), entity.getUser().getEmail(), BigDecimal.valueOf(entity.getAmountCent(), 2));
    }
}
