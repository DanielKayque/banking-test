package com.api.banking_study.features.account;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/account/me")
@Tag(name = "Contas", description = "Endpoints de gerenciamento da conta")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Saldo encontrado com sucesso"),
        //TODO: tratar o retorno de errorResponseDTO(400) para code correto.
        @ApiResponse(responseCode = "400", description = "Conta não identificada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Token inválido ou ausente", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
})
public class CheckBalanceController {

    @Autowired
    private CheckBalanceUseCase usecase;

    @Operation(summary = "Verificar saldo da conta", description = "Endpoint para verificar o saldo atual da conta")
    @GetMapping
    public ResponseEntity<CheckBalanceResponseDto> checkBalance(@AuthenticationPrincipal UserEntity user){
        if (user == null) {
            throw new IllegalArgumentException("Authenticated user is required");
        }

        CheckBalanceResponseDto balance = usecase.execute(user.getId());

        return ResponseEntity.ok().body(balance);
    }
}
