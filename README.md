# Banking API

API REST de uma aplicação bancária de estudos, desenvolvida com Java 21 e Spring Boot. O projeto pratica autenticação, persistência relacional, operações de conta e testes automatizados. Não é destinado a movimentar dinheiro real.

> Projeto em desenvolvimento. O roadmap e as regras de negócio podem mudar conforme novas funcionalidades forem implementadas.

## Funcionalidades implementadas

- Cadastro de usuário com senha armazenada usando BCrypt;
- Login com access token JWT e refresh token;
- Rotação de refresh tokens, detecção de reutilização e logout;
- Criação de uma conta por usuário, iniciada com saldo zero;
- Consulta do saldo da conta autenticada;
- Depósito e transferência entre contas, com registros no histórico;
- Consulta paginada do histórico de transações;
- PostgreSQL com migrations Flyway;
- Documentação OpenAPI/Swagger.

O saque como operação própria ainda não está implementado. Consulte [BANKING_ROADMAP.md](BANKING_ROADMAP.md) para o estado detalhado e os próximos passos.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Data JPA e Spring Security
- Bean Validation
- PostgreSQL e Flyway
- JWT (java-jwt)
- Gradle Wrapper
- Docker Compose
- JUnit e Mockito
- Lombok

## Requisitos

- JDK 21 ou superior
- Docker com Docker Compose

O repositório inclui o Gradle Wrapper, então não é necessário instalar o Gradle globalmente.

## Configuração local

Clone o repositório e entre na pasta do projeto:

```bash
git clone https://github.com/DanielKayque/banking-test.git
cd banking-test
```

Crie um arquivo `.env` baseado no exemplo incluído:

```bash
cp .env.example .env
```

No Windows PowerShell, use `Copy-Item .env.example .env`.

Configure as variáveis no `.env`:

```env
JWT_SECRET=troque-por-um-segredo-local
DB_NAME=mydatabase
DB_URL=jdbc:postgresql://localhost:5432/mydatabase
DB_USERNAME=myuser
DB_PASSWORD=troque-esta-senha
JWT_ACCESS_TOKEN_EXPIRATION_SECONDS=900
JWT_REFRESH_TOKEN_EXPIRATION_SECONDS=2592000
```

`JWT_ACCESS_TOKEN_EXPIRATION_SECONDS` e `JWT_REFRESH_TOKEN_EXPIRATION_SECONDS` são opcionais; os valores acima correspondem aos padrões da aplicação. Use segredos próprios no ambiente local e nunca coloque credenciais reais no repositório.

Suba o PostgreSQL:

```bash
docker compose up -d
```

Inicie a aplicação:

```bash
./gradlew bootRun
```

No Windows:

```powershell
gradlew.bat bootRun
```

O servidor Spring Boot usa a porta `8080` por padrão. As migrations do Flyway são aplicadas durante a inicialização.

## Documentação da API

Com a aplicação em execução, acesse o Swagger UI em [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html). A especificação OpenAPI fica em [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

Os endpoints de autenticação são públicos. Os endpoints de conta e transações exigem o access token JWT no cabeçalho:

```http
Authorization: Bearer <accessToken>
```

### Autenticação

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/auth/register` | Cadastra um usuário; responde `201` |
| `POST` | `/auth/login` | Autentica e retorna access e refresh tokens |
| `POST` | `/auth/refresh` | Revoga o refresh token usado e retorna um novo par de tokens |
| `POST` | `/auth/logout` | Revoga o refresh token; responde `204` |

Exemplo de cadastro:

```json
{
  "username": "Maria Silva",
  "email": "maria.silva@example.com",
  "password": "Senha@123",
  "document": "12345678901"
}
```

Exemplo de login:

```json
{
  "email": "maria.silva@example.com",
  "password": "Senha@123"
}
```

O login retorna `accessToken`, `refreshToken`, `tokenType`, `expiresIn` e `refreshExpiresIn`. O refresh token deve ser enviado no corpo das requisições de renovação e logout:

```json
{
  "refreshToken": "<refreshToken>"
}
```

O logout invalida o refresh token. O access token emitido anteriormente permanece válido até expirar.

### Conta e transações

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/account` | Cria a conta do usuário autenticado, com saldo inicial zero; responde `201` |
| `GET` | `/account/me` | Consulta o saldo da conta autenticada |
| `POST` | `/account/deposit` | Deposita na conta autenticada |
| `POST` | `/account/transfer` | Transfere para uma conta identificada pelo documento de destino |
| `GET` | `/transactions?page=0&size=10` | Lista o histórico paginado da conta autenticada |

Exemplo de depósito:

```json
{
  "amount": 100.00,
  "description": "Depósito de teste"
}
```

Exemplo de transferência:

```json
{
  "documentDestination": "98765432100",
  "amount": 25.50
}
```

Os valores monetários são representados em reais na API e armazenados em centavos. Como uma conta nova começa com saldo zero, faça um depósito antes de tentar transferir.

O histórico aceita `page` (começa em zero) e `size` (tamanho da página). Os resultados são ordenados do mais recente para o mais antigo. Transferências aparecem usando os tipos genéricos `WITHDRAW` e `DEPOSIT`.

## Executar os testes

Linux/macOS:

```bash
./gradlew test
```

Windows:

```powershell
gradlew.bat test
```

## Estrutura

```text
src/
├── main/
│   ├── java/com/api/banking_study/
│   │   ├── config/       # Configurações da aplicação e OpenAPI
│   │   ├── entities/     # Entidades JPA
│   │   ├── exceptions/   # Exceções e tratamento global
│   │   ├── features/     # Fluxos de usuário, conta e transações
│   │   ├── repository/   # Repositórios Spring Data
│   │   └── security/     # JWT e configuração de segurança
│   └── resources/db/migration/ # Migrations Flyway
└── test/                 # Testes automatizados
```

## Roadmap

Entre os próximos passos estão implementar saque, reforçar os testes de concorrência e rollback, adicionar idempotência para transferências e incluir filtros no extrato. O [roadmap completo](BANKING_ROADMAP.md) mantém o acompanhamento atualizado.

## Autor

**Daniel Kayque** · [GitHub](https://github.com/DanielKayque)
