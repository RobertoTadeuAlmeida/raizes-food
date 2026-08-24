# Raízes Food

Raízes Food é o backend acadêmico de uma rede fictícia de lanchonetes chamada Raízes do Nordeste. O projeto implementa uma API REST para exercitar o fluxo principal de pedidos, desde a consulta do cardápio e a criação do pedido até o pagamento, a atualização de status e o cancelamento.

## Funcionalidades implementadas

- cadastro de clientes e login;
- autenticação por Bearer Token e autorização por perfil;
- consulta do cardápio disponível em uma unidade;
- criação, consulta, filtro e cancelamento de pedidos;
- processamento de pagamento por um gateway mock;
- baixa e retorno de estoque conforme o fluxo do pedido;
- avanço controlado do status do pedido;
- registro de eventos de criação, pagamento, alteração de status e cancelamento;
- respostas padronizadas para erros de argumento e conflitos de regra de negócio.

## Tecnologias

- Java 21;
- Spring Boot 4.1.1;
- Spring Web MVC;
- Spring Data JPA;
- Spring Security;
- Bean Validation;
- PostgreSQL;
- Maven Wrapper;
- Springdoc OpenAPI/Swagger UI;
- JUnit 5, Mockito e AssertJ nos testes.

## Estrutura do projeto

O código segue uma arquitetura em camadas:

```text
src/main/java/br/com/raizesfood
├── config       # configuração do Spring Security
├── controller   # endpoints REST
├── dto          # objetos de entrada e saída da API
├── exception    # tratamento global de erros
├── gateway      # contrato e implementação mock do pagamento
├── model        # entidades JPA e enums do domínio
├── repository   # acesso a dados com Spring Data JPA
├── security     # filtro de autenticação por token
└── service      # casos de uso e regras de negócio
```

A documentação de requisitos, modelagem, fluxo crítico e arquitetura está em [`docs/`](docs/).

## Pré-requisitos

- JDK 21;
- PostgreSQL em execução;
- terminal compatível com o Maven Wrapper (`mvnw` ou `mvnw.cmd`).

Não é necessário instalar o Maven separadamente.

## Configuração do PostgreSQL

A aplicação utiliza estas configurações, definidas em `src/main/resources/application.yaml`:

```yaml
url: jdbc:postgresql://localhost:5432/raizes_food
username: raizes_food
password: ${DB_PASSWORD}
```

Crie o usuário e o banco localmente. No `psql`, conectado com um usuário que tenha permissão administrativa, execute:

```sql
CREATE USER raizes_food WITH PASSWORD 'sua_senha_local';
CREATE DATABASE raizes_food OWNER raizes_food;
```

Defina `DB_PASSWORD` com a mesma senha usada na criação do usuário.

Linux/macOS:

```bash
export DB_PASSWORD='sua_senha_local'
```

Windows PowerShell:

```powershell
$env:DB_PASSWORD = 'sua_senha_local'
```

Essa é a única variável de ambiente referenciada pela configuração atual. O Hibernate usa `ddl-auto: update`, portanto cria ou atualiza as tabelas ao iniciar a aplicação. O projeto não possui migrations nem carga automática de dados.

## Como executar

Com o PostgreSQL disponível e `DB_PASSWORD` definida, execute na raiz do projeto.

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Sem outra configuração de porta, a API fica disponível em `http://localhost:8080`.

Para usar os fluxos de cardápio, pedido e pagamento, o banco precisa conter previamente uma unidade, produtos e seus registros de estoque. A API atual não possui endpoints para cadastrar esses dados.

## Testes

Execute toda a suíte com:

```bash
./mvnw test
```

No Windows, use `.\mvnw.cmd test`.

Há testes unitários com Mockito para os serviços de pedido, pagamento e estoque, além de testes de contexto e repositories com `@SpringBootTest`. Por isso, para executar a suíte completa, o PostgreSQL e a variável `DB_PASSWORD` devem estar configurados.

## Swagger/OpenAPI

Com a aplicação em execução, acesse:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- especificação OpenAPI em JSON: `http://localhost:8080/v3/api-docs`

Essas rotas são públicas na configuração de segurança.

## Autenticação e autorização

O cadastro cria sempre um usuário com perfil `CLIENTE`, e a senha é armazenada usando BCrypt. O login devolve um token opaco gerado pela aplicação. Envie esse valor nas rotas protegidas:

```http
Authorization: Bearer SEU_TOKEN
```

Os tokens ficam somente em memória e deixam de ser válidos quando a aplicação é reiniciada. Não há expiração ou renovação implementada.

Os perfis existentes são `CLIENTE`, `GERENTE` e `ADMINISTRADOR`. Cadastro, login e documentação OpenAPI são públicos. A alteração de status de um pedido exige `GERENTE` ou `ADMINISTRADOR`; os demais endpoints abaixo exigem apenas um usuário autenticado e ativo.

## Endpoints implementados

### Autenticação

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/auth/cadastro` | Público | Cadastra um cliente. |
| `POST` | `/auth/login` | Público | Autentica por e-mail e senha e retorna o token. |

### Cardápio

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/unidades/{unidadeId}/cardapio` | Autenticado | Lista produtos ativos com estoque maior que zero na unidade. |

### Pedidos

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/pedidos` | Autenticado | Cria um pedido em `PENDENTE_PAGAMENTO`. |
| `GET` | `/pedidos/{id}` | Autenticado | Consulta um pedido pelo ID. |
| `GET` | `/pedidos?canalPedido={canal}` | Autenticado | Lista pedidos pelo canal informado. |
| `PATCH` | `/pedidos/{id}/status` | `GERENTE` ou `ADMINISTRADOR` | Avança o status conforme o fluxo permitido. |
| `POST` | `/pedidos/{id}/cancelamento` | Autenticado | Cancela um pedido conforme o perfil do usuário autenticado e o status atual do pedido. |
Os canais aceitos são `APP`, `TOTEM`, `BALCAO`, `PICKUP` e `WEB`. O avanço manual segue `PAGAMENTO_APROVADO` → `EM_PREPARACAO` → `PRONTO` → `FINALIZADO`.

### Pagamento

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/pedidos/{pedidoId}/pagamento` | Autenticado | Processa uma tentativa de pagamento e, quando aprovada, baixa o estoque. |

O gateway mock atual sempre retorna `APROVADO`. A estrutura do serviço também trata `RECUSADO`, mantendo o pedido pendente e sem baixar o estoque.

## Coleção Postman

A coleção está em [`postman/Raízes Food - MVP.postman_collection.json`](postman/Ra%C3%ADzes%20Food%20-%20MVP.postman_collection.json).

Para utilizá-la:

1. No Postman, selecione **Import** e escolha o arquivo da coleção.
2. Nas variáveis da coleção, defina `baseUrl` como `http://localhost:8080`.
3. Execute **Login**. Essa requisição salva automaticamente a resposta em `token`.
4. Execute **Criar Pedido**. Essa requisição salva o ID retornado em `pedidoId`.
5. Ajuste os IDs e dados dos exemplos para os registros existentes no seu banco.

A coleção herda `Authorization: Bearer {{token}}` nas requisições protegidas e possui `tokenGerente` para a atualização de status.

## Segurança e tratamento de erros

A API não mantém sessão HTTP: cada requisição protegida precisa enviar o Bearer Token. Apenas usuários ativos são autenticados, e senhas cadastradas são codificadas com BCrypt.

Erros lançados pelas regras implementadas possuem resposta JSON com `status`, `erro`, `mensagem` e `timestamp`. Argumentos inválidos retornam HTTP `400 Bad Request`; conflitos de estado, como estoque insuficiente ou transição inválida, retornam HTTP `409 Conflict`.

## Escopo acadêmico

Este repositório representa um MVP acadêmico. O foco atual é o fluxo crítico de pedido, pagamento mock e consistência de estoque. Recursos como migrations, carga inicial de dados, persistência ou expiração de tokens e endpoints administrativos para manter unidades, produtos e estoque ainda não fazem parte da implementação atual.

## Repositório

Código-fonte e documentação do projeto:

https://github.com/RobertoTadeuAlmeida/raizes-food