# Roadmap do Projeto Bancário

Este documento acompanha o estado da API de estudo e organiza os próximos passos para evoluí-la com segurança, consistência financeira e futura integração com Flutter.

## Estado atual

### Implementado

- Cadastro de usuário e autenticação com JWT;
- Senhas armazenadas com BCrypt;
- Uma conta por usuário, com saldo em centavos;
- Criação de conta e consulta do saldo autenticado (`GET /account/me`);
- Depósito (`POST /account/deposit`) com atualização de saldo e registro de transação na mesma transação do banco;
- Transferência entre contas por documento, com débito, crédito e registros de saída/entrada atômicos;
- Histórico de transações do usuário autenticado (`GET /transactions`), paginado e ordenado da mais recente para a mais antiga;
- Persistência PostgreSQL com migrations Flyway;
- Testes automatizados iniciais;
- Docker Compose para PostgreSQL.

### Parcial ou pendente

- O histórico registra depósitos, saques e transferências, mas transferências são representadas pelos tipos genéricos `DEPOSIT` e `WITHDRAW`;
- A consulta de transações ainda não oferece filtros por período ou tipo nem resumo de entradas e saídas;
- O depósito rejeita valores que não possam ser convertidos exatamente para centavos, mas ainda não define limite máximo explícito;
- O repositório usa bloqueio pessimista nas consultas de conta usadas pelas operações financeiras; ainda faltam regras completas de concorrência e testes para cenários simultâneos;
- Refresh token com persistência como hash, rotação, detecção de reutilização e logout estão implementados; o access token é curto e sua validade é configurável. Ainda faltam logout de todas as sessões e limite de tentativas;
- O documento/CPF é verificado quanto à duplicidade, mas a validação e normalização do formato ainda precisam ser definidas.

## Objetivo do MVP bancário

O MVP deve permitir cadastro e login, abertura e consulta de conta, depósito, saque, transferência e consulta paginada do extrato. Cadastro, login, abertura, saldo, depósito, transferência e paginação básica do extrato já estão implementados. O saque como operação própria e os aprimoramentos do extrato seguem pendentes.

## Fase 1 — Conta e saldo — Concluída

- `GET /account/me` retorna o saldo da conta do usuário autenticado;
- As operações da conta partem da identidade autenticada, sem permitir escolher livremente outra conta para consulta.

## Fase 2 — Registro e consulta de transações — Parcialmente concluída

### Implementado

- Tabela e entidade de transações com conta, tipo, valor em centavos, descrição e data;
- Depósitos e transferências persistem seus lançamentos junto com a alteração de saldo;
- `GET /transactions` retorna apenas o histórico da conta do usuário autenticado, com paginação e ordenação decrescente por data.

### Próximos passos

- Definir tipos próprios para transferência enviada e recebida, se isso melhorar a leitura do extrato;
- Adicionar filtros por período e tipo de transação;
- Exibir resumo paginado de entradas e saídas;
- Definir política de imutabilidade e retenção do histórico.

## Fase 3 — Saque — Pendente

O domínio já possui `AccountEntity.withdraw`, incluindo validação de valor positivo e saldo suficiente, e o repositório já oferece bloqueio pessimista. Falta expor a operação como caso de uso e endpoint.

- Criar `POST /account/withdraw`;
- Validar valor positivo e conversão exata para centavos;
- Debitar a conta autenticada e registrar a transação de saque atomicamente;
- Cobrir sucesso, saldo insuficiente, valor inválido e concorrência com testes.

## Fase 4 — Robustez de depósitos — Parcialmente concluída

O depósito já atualiza o saldo e registra a transação em uma única transação de banco. A consulta da conta é feita com bloqueio pessimista.

- Definir e validar valor máximo por operação;
- Manter validação para valores positivos e no máximo duas casas decimais;
- Adicionar testes de limite, falha de persistência e operações concorrentes.

## Fase 5 — Transferências — Parcialmente concluída

Existe `POST /account/transfer`, que localiza o destino pelo documento, debita a origem, credita o destino e salva os dois lançamentos dentro de uma transação de banco.

- Impedir explicitamente transferência para a própria conta;
- Adicionar chave de idempotência para evitar duplicidade em repetição de requisições;
- Definir bloqueio das duas contas em ordem consistente para reduzir risco de deadlock;
- Validar limite e precisão do valor;
- Distinguir transferência enviada/recebida no histórico;
- Cobrir concorrência, rollback e repetição da mesma solicitação com testes.

## Fase 6 — Extrato — Parcialmente concluída

A paginação e a ordenação mais recente primeiro estão implementadas. Filtros e resumos permanecem pendentes, conforme a Fase 2.

## Fase 7 — Segurança e autenticação avançada — Parcialmente concluída

JWT, BCrypt e segredo configurado por propriedade já existem. O perfil de desenvolvimento obtém o segredo por variável de ambiente. O refresh token é opaco, aleatório, persistido somente como hash, rotacionado a cada renovação e revogado no logout. O access token tem validade curta configurável e é validado com uma finalidade distinta do refresh token.

- Implementar logout de todas as sessões do usuário;
- Adicionar limite de tentativas de login;
- Validar e normalizar documento/CPF;
- Revisar mensagens de erro para evitar exposição de dados sensíveis;
- Configurar CORS para o cliente mobile quando a origem for conhecida;
- Escolher se JWT próprio continuará como mecanismo principal ou se Keycloak será adotado.

## Fase 8 — Perfil do usuário — Pendente

- Consultar e atualizar nome e e-mail;
- Alterar senha;
- Desativar conta;
- Exigir confirmação para operações sensíveis.

## Fase 9 — Recursos extras — Futuro

- Notificações de transferências;
- Alertas de saldo baixo;
- Categorias de gastos e metas financeiras;
- Resumos mensais e gráficos de entradas e saídas.

## Próxima ordem recomendada

```text
Saque com histórico
        ↓
Regras e testes de valor/concorrência para depósito e saque
        ↓
Idempotência e consistência de transferências
        ↓
Filtros e resumos do extrato
        ↓
Segurança avançada
        ↓
Perfil do usuário e recursos extras
```

## Integração futura com Flutter

Com os fluxos principais da API estabilizados, o aplicativo pode oferecer login, cadastro, armazenamento seguro do token, saldo, depósito, saque, transferência, extrato, tratamento de erros e logout/expiração de sessão.

## Cuidados do projeto

Este projeto é um sistema bancário virtual para estudo, não uma aplicação financeira real. Um uso real exigiria requisitos regulatórios, auditorias, controles antifraude, monitoramento, recuperação de desastre e outras camadas de segurança.
