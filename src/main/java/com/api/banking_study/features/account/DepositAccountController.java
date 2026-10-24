package com.api.banking_study.features.account;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.AuthenticatedUserRequiredException;
import com.api.banking_study.exceptions.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contas", description = "Endpoints de gerenciamento da conta")
@SecurityRequirement(name = "bearerAuth")
public class DepositAccountController {
    @Autowired
    private DepositAccountUseCase useCase;

    @Operation(summary = "Depositar", description = "Depositar valor na sua conta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor depositado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Valor inválido ou conta não identificada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido")
    })
    @PostMapping(value = "/deposit")
    public ResponseEntity<AccountResponseDto> deposit(@AuthenticationPrincipal UserEntity user,
                                                      @Parameter(description = "Valor a ser depositado")
                                                      @RequestBody @Valid DepositRequestDto dto){

        if (user == null) {
            throw new AuthenticatedUserRequiredException("Authenticated user is required");
        }

        BigDecimal amount = useCase.execute(user.getId(), dto.amount(), dto.description());

        AccountResponseDto responseDto = new AccountResponseDto(user.getUsername(), user.getEmail(), amount);

        return ResponseEntity.ok().body(responseDto);
    }
}
