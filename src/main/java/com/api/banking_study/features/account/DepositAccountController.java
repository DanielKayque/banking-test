package com.api.banking_study.features.account;

import com.api.banking_study.entities.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class DepositAccountController {
    @Autowired
    private DepositAccountUseCase useCase;

    @PostMapping(value = "/deposit")
    public ResponseEntity<AccountResponseDto> deposit(@AuthenticationPrincipal UserEntity user, @RequestBody @Valid DepositRequestDto dto){

        BigDecimal amount = useCase.execute(user.getId(), dto.amount());

        AccountResponseDto responseDto = new AccountResponseDto(user.getUsername(), user.getEmail(), amount);

        return ResponseEntity.ok().body(responseDto);
    }
}
