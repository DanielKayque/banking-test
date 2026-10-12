package com.api.banking_study.features.account;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@Tag(name = "Contas", description = "Endpoints de gerenciamento da conta")
@SecurityRequirement(name = "bearerAuth")
public class CreateAccountController {

    @Autowired
    private CreateAccountUseCase usecase;

    @PostMapping
    @Operation(summary = "Criar conta", description = "Crie a sua conta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @ApiResponse(responseCode = "409", description = "Usuário já possui uma conta", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<AccountResponseDto> createAccount(@AuthenticationPrincipal UserEntity user){
        if (user == null) {
            throw new IllegalArgumentException("Authenticated user is required");
        }

        AccountResponseDto result = usecase.execute(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
