package com.api.banking_study.features.account;

import com.api.banking_study.entities.TransactionEntity;
import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.AuthenticatedUserRequiredException;
import com.api.banking_study.exceptions.InvalidDestinationAccountException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/transfer")
@Tag(name = "Transferência entre contas", description = "Endpoint que realiza transferência entre contas")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Operação realizada", content = @Content(schema = @Schema(implementation = AccountTransferResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou insuficientes", content = @Content(schema = @Schema(implementation = AccountTransferResponseDto.class)))
})
public class AccountTransferController {

    @Autowired
    private AccountTransferUseCase useCase;

    @PostMapping
    @Operation(summary = "Transferência entre contas", description = "Realize transferências entre contas")
    public ResponseEntity<AccountTransferResponseDto> transfer(@AuthenticationPrincipal UserEntity user, @RequestBody @Valid AccountTransferRequestDto request){
        if (user == null) {
            throw new AuthenticatedUserRequiredException("Authenticated user is required");
        }

        if (user.getDocument().equals(request.documentDestination())) {
            throw new InvalidDestinationAccountException("Invalid destination account");
        }

        AccountTransferResponseDto entity = useCase.execute(user.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }
}
