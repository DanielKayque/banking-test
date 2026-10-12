package com.api.banking_study.features.account;

import com.api.banking_study.entities.AccountEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.repository.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class CreateAccountController {

    @Autowired
    private CreateAccountUseCase usecase;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody @Valid AccountRequestDto dto){
        if (dto.document() == null){
            throw new IllegalArgumentException("Invalid email or document");
        }

        AccountResponseDto result = usecase.execute(dto.document());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
