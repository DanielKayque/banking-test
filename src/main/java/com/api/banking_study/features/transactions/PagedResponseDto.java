package com.api.banking_study.features.transactions;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PagedResponseDto<T>(
        @ArraySchema(schema = @Schema(implementation = ListTransactionsResponseDto.class))
        List<T> content,
        @Schema(description = "Quantidade total de transações", example = "25")
        Long totalElements,
        @Schema(description = "Quantidade total de páginas", example = "3")
        int totalPages
) {
}
