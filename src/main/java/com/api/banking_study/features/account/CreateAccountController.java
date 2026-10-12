package com.api.banking_study.features.account;

import com.api.banking_study.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class CreateAccountController {

    @Autowired
    private CreateAccountUseCase usecase;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@AuthenticationPrincipal UserEntity user){

        AccountResponseDto result = usecase.execute(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
