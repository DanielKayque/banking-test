# Banking API

Projeto backend de uma aplicação bancária desenvolvido com **Java 21 e Spring Boot**, com foco na prática de conceitos modernos de desenvolvimento backend, como APIs REST, autenticação, persistência de dados, migrations, validações e organização da aplicação.

> 🚧 **Projeto em desenvolvimento**
>
> Este projeto está em constante evolução. Funcionalidades, regras de negócio, endpoints e decisões arquiteturais podem sofrer alterações ao longo do desenvolvimento.

## Sobre o projeto

A Banking API é um projeto pessoal criado com o objetivo de simular algumas das principais responsabilidades de um sistema bancário.

O projeto foi desenvolvido principalmente para consolidar conhecimentos no ecossistema Java e aplicar conceitos comuns em aplicações backend reais, com foco em:

- organização de responsabilidades;
- segurança;
- persistência de dados;
- regras de negócio;
- boas práticas de desenvolvimento;
- testes automatizados;
- evolução incremental da arquitetura.

Novas funcionalidades e melhorias são adicionadas conforme o projeto avança.

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Data JPA**
- **Spring Security**
- **Bean Validation**
- **PostgreSQL**
- **Flyway**
- **JWT**
- **Docker Compose**
- **Gradle**
- **Lombok**
- **JUnit**

## Conceitos aplicados

Este projeto também serve como ambiente de prática para conceitos importantes de desenvolvimento backend, como:

- Desenvolvimento de APIs REST
- Autenticação e autorização
- Segurança baseada em JWT
- Persistência de dados com JPA/Hibernate
- Migrations de banco de dados com Flyway
- Uso de DTOs
- Validação de dados
- Tratamento global de exceções
- Injeção de dependências
- Separação entre regras de negócio e infraestrutura
- Configuração por variáveis de ambiente
- Banco de dados executado com Docker
- Testes unitários e de integração

## Estrutura do projeto

A aplicação busca manter uma organização que facilite a separação entre regras de negócio, infraestrutura e responsabilidades da aplicação.

```text
src/
├── main/
│   ├── java/
│   │   └── com/api/
│   │       └── ...
│   │
│   └── resources/
│       ├── db/
│       │   └── migration/
│       └── ...
│
└── test/
    └── java/
```

Como o projeto ainda está em desenvolvimento, a estrutura interna e a organização dos pacotes podem ser refatoradas futuramente.

## Banco de dados

O projeto utiliza **PostgreSQL** como banco de dados relacional.

As alterações na estrutura do banco são gerenciadas com **Flyway**, permitindo que o schema seja versionado e evolua de forma controlada.

O ambiente de desenvolvimento também utiliza **Docker Compose** para facilitar a execução do PostgreSQL localmente.

## Segurança

A aplicação utiliza **Spring Security** e autenticação baseada em **JWT**.

A camada de segurança é responsável por controlar o acesso aos recursos da API e permitir a evolução futura de regras de autorização baseadas em usuários e permissões.

As funcionalidades relacionadas à segurança ainda estão sendo aprimoradas conforme o desenvolvimento do projeto avança.

## Variáveis de ambiente

Crie um arquivo `.env` baseado no arquivo `.env.example` disponível no projeto.

Exemplo:

```env
JWT_SECRET=change-me

DB_NAME=mydatabase
DB_URL=jdbc:postgresql://localhost:5432/mydatabase
DB_USERNAME=myuser
DB_PASSWORD=change-me
```

Nunca envie credenciais reais ou segredos de produção para o repositório.

## Executando o projeto

### Requisitos

Tenha instalado:

- Java 21+
- Docker
- Docker Compose

O projeto já possui o **Gradle Wrapper**, portanto não é necessário instalar o Gradle globalmente.

### Clone o repositório

```bash
git clone https://github.com/DanielKayque/banking-test.git

cd banking-test
```

### Configure as variáveis de ambiente

Crie o arquivo `.env` com base no exemplo:

```bash
cp .env.example .env
```

Depois, ajuste as variáveis de acordo com o seu ambiente local.

### Inicie o PostgreSQL

```bash
docker compose up -d
```

### Execute a aplicação

Linux/macOS:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

## Executando os testes

Linux/macOS:

```bash
./gradlew test
```

Windows:

```bash
gradlew.bat test
```

## Status atual

O projeto ainda está em desenvolvimento.

Entre os pontos que estão sendo implementados ou aprimorados estão:

- Regras de negócio bancárias
- Gerenciamento de contas
- Autenticação e autorização
- Validação de dados
- Tratamento de exceções
- Modelagem do banco de dados
- Testes automatizados
- Regras de segurança
- Organização da arquitetura da aplicação

## Próximos passos

Algumas melhorias planejadas:

- [ ] Expandir as operações bancárias
- [ ] Aprimorar autenticação e autorização
- [ ] Aumentar a cobertura de testes unitários e de integração
- [ ] Melhorar validações de domínio
- [ ] Adicionar documentação da API
- [ ] Padronizar respostas de erro
- [ ] Evoluir a arquitetura da aplicação
- [ ] Implementar regras bancárias mais próximas de cenários reais
- [ ] Melhorar o ambiente local com Docker
- [ ] Preparar a aplicação para um ambiente semelhante ao de produção

## Objetivo

Este repositório é principalmente um **projeto de estudos e portfólio**.

Seu objetivo é demonstrar a evolução dos meus conhecimentos em desenvolvimento backend utilizando **Java, Spring Boot, PostgreSQL e tecnologias relacionadas**, aplicando conceitos encontrados em aplicações reais.

O projeto continuará evoluindo conforme novos conhecimentos, padrões arquiteturais e funcionalidades forem estudados e implementados.

## Autor

**Daniel Kayque**

GitHub: [DanielKayque](https://github.com/DanielKayque)

---

Projeto em desenvolvimento e sujeito a melhorias contínuas.
