# Plano de Testes

O objetivo deste plano é validar os principais fluxos do MVP do Raízes Food, com foco em autenticação, autorização,
pedidos, pagamento, estoque e tratamento de erros.

## Cenários principais

### CT001 — Login com credenciais válidas

**Objetivo:** validar a autenticação de um usuário cadastrado.

**Pré-condição:** usuário ativo cadastrado no sistema.

**Execução:**

- enviar e-mail e senha válidos para `/auth/login`.

**Resultado esperado:**

- resposta HTTP 200;
- retorno de token;
- retorno do perfil do usuário.

**Situação:** Validado.

---

### CT002 — Login com senha inválida

**Objetivo:** impedir autenticação com senha incorreta.

**Execução:**

- enviar e-mail válido e senha incorreta.

**Resultado esperado:**

- resposta HTTP 400;
- nenhum token gerado.

**Situação:** Validado.

---

### CT003 — Acesso sem token

**Objetivo:** validar proteção das rotas autenticadas.

**Execução:**

- consultar um Pedido sem enviar `Authorization`.

**Resultado esperado:**

- acesso negado.

**Situação:** Validado.

---

### CT004 — Autorização por perfil

**Objetivo:** validar que um Cliente não pode atualizar o status operacional do Pedido.

**Execução:**

- autenticar como CLIENTE;
- solicitar alteração do status de um Pedido.

**Resultado esperado:**

- resposta HTTP 403.

**Situação:** Validado.

---

### CT005 — Gerente atualiza status

**Objetivo:** validar acesso autorizado ao fluxo operacional.

**Pré-condição:** usuário autenticado com perfil GERENTE.

**Execução:**

- alterar Pedido de `PAGAMENTO_APROVADO` para `EM_PREPARACAO`.

**Resultado esperado:**

- resposta HTTP 200;
- Pedido atualizado;
- evento registrado.

**Situação:** Validado.

---

### CT006 — Criar Pedido válido

**Objetivo:** validar a criação de um Pedido com estoque disponível.

**Execução:**

- informar Unidade válida;
- informar canal;
- informar Produto e quantidade disponível.

**Resultado esperado:**

- resposta HTTP 201;
- Pedido em `PENDENTE_PAGAMENTO`;
- ItemPedido persistido;
- preço do Produto preservado no ItemPedido;
- evento `PEDIDO_CRIADO`;
- estoque não alterado.

**Situação:** Validado.

---

### CT007 — Consultar Pedido existente

**Objetivo:** validar consulta de Pedido pelo identificador.

**Resultado esperado:**

- resposta HTTP 200;
- dados do Pedido e seus itens.

**Situação:** Validado.

---

### CT008 — Criar Pedido sem estoque suficiente

**Objetivo:** impedir pedido quando a quantidade solicitada for maior que o estoque disponível.

**Resultado esperado:**

- operação rejeitada;
- Pedido não criado.

**Situação:** Validado por teste automatizado.

---

### CT009 — Pagamento aprovado

**Objetivo:** validar o fluxo de pagamento aprovado pelo Gateway Mock.

**Pré-condição:** Pedido em `PENDENTE_PAGAMENTO`.

**Resultado esperado:**

- tentativa registrada como `APROVADO`;
- baixa do estoque;
- Pedido em `PAGAMENTO_APROVADO`;
- evento registrado.

**Situação:** Validado.

---

### CT010 — Pagamento repetido

**Objetivo:** impedir novo processamento de um Pedido já pago.

**Execução:**

- processar novamente o pagamento de um Pedido aprovado.

**Resultado esperado:**

- resposta HTTP 409;
- nenhuma nova baixa de estoque.

**Situação:** Validado.

---

### CT011 — Pagamento recusado

**Objetivo:** validar que uma tentativa recusada não altera o estoque.

**Resultado esperado:**

- tentativa `RECUSADO`;
- Pedido permanece `PENDENTE_PAGAMENTO`;
- estoque permanece inalterado.

**Situação:** Validado por teste automatizado com Gateway mockado.

---

### CT012 — Cancelamento antes da baixa

**Objetivo:** cancelar Pedido ainda pendente de pagamento.

**Resultado esperado:**

- Pedido em `CANCELADO`;
- estoque não alterado.

**Situação:** Validado por teste automatizado.

---

### CT013 — Cancelamento após pagamento

**Objetivo:** validar retorno de estoque após cancelamento de Pedido que já teve baixa.

**Pré-condição:** Pedido pago e em estado cancelável.

**Resultado esperado:**

- Pedido em `CANCELADO`;
- quantidade devolvida ao estoque;
- evento registrado.

**Situação:** Validado.

---

### CT014 — Cancelamento em status inválido

**Objetivo:** impedir cancelamento após o Pedido atingir estado não cancelável.

**Resultado esperado:**

- resposta HTTP 409;
- status e estoque preservados.

**Situação:** Validado.

---

### CT015 — Filtro de Pedidos por canal

**Objetivo:** validar consulta por `canalPedido`.

**Execução:**

- consultar pedidos com `canalPedido=TOTEM`.

**Resultado esperado:**

- resposta HTTP 200;
- somente pedidos do canal informado.

**Situação:** Validado.

---

### CT016 — Filtro por canal sem resultados

**Execução:**

- consultar canal sem Pedidos cadastrados.

**Resultado esperado:**

- resposta HTTP 200;
- lista vazia.

**Situação:** Validado.

---

### CT017 — Cardápio por Unidade

**Objetivo:** validar consulta do cardápio disponível em uma Unidade.

**Resultado esperado:**

- resposta HTTP 200;
- somente Produtos ativos com estoque maior que zero;
- preço e quantidade disponível apresentados.

**Situação:** Validado.

---

## Testes automatizados

A suíte possui testes para:

- persistência com Spring Data JPA;
- criação de Pedido;
- preservação do preço do ItemPedido;
- disponibilidade de Estoque;
- entrada, baixa e retorno de Estoque;
- pagamento aprovado;
- pagamento recusado;
- transições válidas e inválidas de status;
- cancelamento conforme perfil e status.

A suíte pode ser executada com:

```bash
./mvnw test
```

- Os testes de integração que utilizam o contexto Spring dependem do PostgreSQL local e da variável de ambiente `DB_PASSWORD.`

### Evidências manuais

- Os principais fluxos também foram executados pela coleção Postman disponível em:
`postman/Raízes Food - MVP.postman_collection.json`

- A coleção contempla autenticação, criação e consulta de Pedido, pagamento, alteração de status, cancelamento, filtro por
canal, cardápio e acesso sem autenticação.