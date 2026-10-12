package com.api.banking_study.features.user;

import com.api.banking_study.exceptions.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Cadastro e autenticação de usuários")
public class RegisterUserController {
    @Autowired
    private RegisterUserUseCase useCase;

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário e retorna os dados públicos do cadastro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "E-mail ou documento já cadastrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserDto userDto){

        UserResponseDTO result = useCase.execute(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
