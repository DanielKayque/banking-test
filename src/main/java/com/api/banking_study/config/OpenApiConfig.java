package com.api.banking_study.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Banking Study API",
                version = "v1",
                description = "API para cadastro de usuários, autenticação e operações básicas de conta bancária.",
                contact = @Contact(name = "Banking Study")
        ),
        servers = @Server(url = "/", description = "Servidor atual")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT retornado pelo endpoint de login."
)
public class OpenApiConfig {
}
