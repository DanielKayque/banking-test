package com.api.banking_study.features.user;

import com.api.banking_study.exceptions.ErrorResponseDTO;
import com.api.banking_study.security.TokenService;
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
public class LoginController {

    @Autowired
    private LoginUseCase useCase;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Valida as credenciais e retorna um token JWT para acessar os endpoints protegidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginUserDto data){
            String result = useCase.execute(data);

            LoginResponseDto responseDto = new LoginResponseDto(result, "Bearer", tokenService.getExpirationDate());

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
