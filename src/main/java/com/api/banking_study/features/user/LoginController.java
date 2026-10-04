package com.api.banking_study.features.user;

import com.api.banking_study.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginUseCase useCase;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginUserDto data){
            String result = useCase.execute(data);

            LoginResponseDto responseDto = new LoginResponseDto(result, "Bearer", tokenService.getExpirationDate());

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
