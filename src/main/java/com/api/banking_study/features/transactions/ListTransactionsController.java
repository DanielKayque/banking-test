package com.api.banking_study.features.transactions;

import com.api.banking_study.entities.UserEntity;
import com.api.banking_study.exceptions.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@Validated
@Tag(name = "Transações", description = "Log de transações da conta.")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Histórico de transações retornado com sucesso",
                content = @Content(schema = @Schema(implementation = PagedResponseDto.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Parâmetros de paginação inválidos.",
                content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Conta não encontrada",
                content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Token ausente ou inválido",
                content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
})
public class ListTransactionsController {
    @Autowired
    private ListTransactionsUseCase useCase;

    @Operation(
            summary = "Consultar transações",
            description = "Retorna o histórico paginado de transações da conta do usuário autenticado."
    )
    @GetMapping
    public ResponseEntity<PagedResponseDto<ListTransactionsResponseDto>> list(
            @AuthenticationPrincipal UserEntity user,
            @Parameter(description = "Número da página, começando em 0", example = "0")
            @RequestParam(defaultValue = "0")
            @PositiveOrZero int page,
            @Parameter(description = "Quantidade de transações por página", example = "10")
            @RequestParam(defaultValue = "10")
            @PositiveOrZero int size){

        Page<ListTransactionsResponseDto> result = useCase.execute(page, size, user.getId());

        PagedResponseDto<ListTransactionsResponseDto> dto = new PagedResponseDto<>(result.getContent(), result.getTotalElements(), result.getTotalPages());

        return ResponseEntity.ok().body(dto);
    }
}
