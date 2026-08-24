# Requisitos Não Funcionais

Atributos de qualidade e restrições técnicas.

## RNF001 — Controle de acesso e autorização

**Descrição:**
O sistema deve garantir que as funcionalidades protegidas sejam acessíveis somente de acordo com o perfil e as permissões definidas nas regras de negócio.

**Categoria:**
Segurança / Autorização

**Critérios/garantias:**
- Cliente consegue acessar somente funcionalidades relacionadas à realização e acompanhamento de seus pedidos.
- Gerente consegue acessar somente funcionalidades administrativas das unidades pelas quais é responsável.
- Administrador consegue acessar funcionalidades administrativas de toda a rede Raízes Food.
- Funcionalidades que exigem autenticação não devem ser disponibilizadas sem a devida autenticação e autorização.
- Atendente e Cozinha são atores operacionais sem perfil de login e não recebem permissões administrativas.
- Tentativas de acesso não autorizado devem ser rejeitadas de forma consistente.

**Requisitos relacionados:**
- RF001 — Autenticar usuário
- RF004 — Solicitar e Autorizar Perfil de Gerente
- RN002 — Autorização por perfil
- RN004 — Aprovação de Gerente
- RN005 — Administrador único da rede
- RN006 — Gerente responsável por unidades

---

## RNF002 — Isolamento de dados por unidade

**Descrição:**
O sistema deve garantir que dados pertencentes a uma unidade não sejam indevidamente acessados ou manipulados por outra unidade.

**Categoria:**
Segurança / Isolamento de Dados



**Critérios/garantias:**
- Gerente consegue consultar e ajustar o estoque somente das unidades sob sua responsabilidade.
- Gerente não consegue acessar dados de estoque de unidades fora de sua responsabilidade.
- Administrador consegue consultar e ajustar o estoque de qualquer unidade.
- Dados históricos de uma unidade permanecem isolados dos dados de outras unidades.

**Requisitos relacionados:**
- RF006 — Gerenciar Unidade
- RF009 — Gerenciar Estoque
- RF011 — Criar Pedido
- RN006 — Gerente responsável por unidades
- RN007 — Unidade com apenas um Gerente
- RN011 — Controle de estoque por unidade
- RN012 — Permissão de gerenciamento do estoque
- RN016 — Unidade do pedido

---

## RNF003 — Rastreabilidade e auditoria

**Descrição:**
O sistema deve garantir o registro dos eventos relevantes do ciclo do pedido conforme definido nas regras de negócio, permitindo rastreabilidade completa das operações.

**Categoria:**
Rastreabilidade / Auditoria



**Critérios/garantias:**
- Eventos relevantes do ciclo do pedido são registrados: criação, tentativa e resultado de pagamento, alteração de status e cancelamento.
- Alterações relevantes do status do pedido são registradas.
- O histórico permite identificar a sequência de eventos de um pedido.
- Operações em contextos sem autenticação (BALCAO, TOTEM, COZINHA) registram eventos sem exigir identificação de funcionário específico.
- O objetivo é rastreabilidade dos eventos do pedido, não criar sistema complexo de observabilidade.

**Requisitos relacionados:**
- RF011 — Criar Pedido
- RF013 — Atualizar Status do Pedido
- RF014 — Cancelar Pedido
- RF015 — Processar Pagamento
- RF017 — Registrar Logs/Auditoria
- RN017 — Canal do pedido
- RN026 — Ciclo de status do pedido
- RN030 — Registro de eventos do pedido

---

## RNF004 — Consistência das operações críticas

**Descrição:**
O sistema deve garantir que operações críticas envolvendo pagamento, estoque e cancelamento mantenham os dados em estado consistente.

**Categoria:**
Confiabilidade / Consistência de Dados



**Critérios/garantias:**
- Retorno de estoque ocorre quando um pedido é cancelado e já havia causado baixa anterior.
- Cancelamento de pedido não deixa dados em estado parcialmente processado.
- Um pedido não pode ter seu estoque baixado mais de uma vez.
- Retorno de estoque após cancelamento corresponde exatamente aos itens baixados do pedido.
- Operações de pagamento e estoque mantêm consistência entre pedido, tentativa de pagamento e quantidade disponível.

**Requisitos relacionados:**
- RF011 — Criar Pedido
- RF015 — Processar Pagamento
- RF014 — Cancelar Pedido
- RN020 — Baixa de estoque após pagamento aprovado
- RN022 — Pagamento aprovado
- RN025 — Apenas um pagamento aprovado por pedido
- RN029 — Retorno ao estoque após cancelamento

---

## RNF005 — Segurança de credenciais

**Descrição:**
O sistema deve garantir que credenciais de autenticação sejam protegidas adequadamente e não sejam armazenadas de forma que permita sua recuperação direta.

**Categoria:**
Segurança / Autenticação



**Critérios/garantias:**
- Senhas não são armazenadas em texto plano.
- Senhas não podem ser recuperadas a partir de dados armazenados.
- Armazenamento de senha utiliza técnica que preserve a confidencialidade da credencial.
- Confirmação de senha durante cadastro não é armazenada como dado persistente.
- Alteração de senha ou autenticação envolve processamento de credenciais de forma segura.

**Requisitos relacionados:**
- RF001 — Autenticar usuário
- RF003 — Cadastrar Cliente
- RN001 — E-mail único

---

## RNF006 — Privacidade e Proteção de Dados

**Descrição**

O sistema deve tratar os dados pessoais dos usuários de forma segura e
compatível com os princípios de privacidade e proteção de dados aplicáveis
ao escopo do projeto.

**Categoria**

Segurança / Privacidade / LGPD

**Critérios / Garantias**

- O sistema deve coletar apenas os dados pessoais necessários para as
  funcionalidades previstas no MVP.

- Senhas não devem ser armazenadas em texto puro, devendo ser protegidas
  por mecanismo seguro de hash.

- Senhas e outras informações sensíveis não devem ser expostas nas
  respostas da API ou registradas em logs.

- O acesso aos dados pessoais deve respeitar as regras de autenticação e
  autorização definidas para cada perfil de usuário.

- Os dados pessoais coletados devem ser utilizados somente para as
  finalidades relacionadas às funcionalidades do sistema.

- Quando aplicável ao fluxo adotado, o sistema deve permitir o registro
  do consentimento relacionado ao tratamento de dados pessoais.

- Estratégias de retenção, exclusão ou anonimização de dados devem ser
  consideradas quando aplicáveis ao ciclo de vida das informações
  mantidas pelo sistema.

**Requisitos relacionados**

- RF001 — Autenticar Usuário
- RF003 — Cadastrar Cliente
- RF004 — Solicitar e Autorizar Perfil de Gerente
- RN001 — Unicidade de e-mail
- RN002 — Autorização por perfil

---

## RNF007 — Desempenho

**Descrição:**
O sistema deve responder às operações do fluxo principal em tempo adequado para o uso esperado da aplicação, inclusive em períodos de maior volume de pedidos.

**Categoria:**
Desempenho

**Critérios/garantias:**
- Operações de consulta, criação e atualização de pedidos devem apresentar tempo de resposta adequado ao uso da aplicação.
- A consulta de cardápio e disponibilidade de produtos não deve exigir o carregamento de dados desnecessários de outras unidades.
- O processamento de um pedido deve considerar apenas os dados necessários da unidade, dos produtos e do estoque envolvidos na operação.
- O sistema deve manter o fluxo principal utilizável em períodos de maior volume de pedidos.
- Consultas e operações relacionadas ao estoque devem utilizar os relacionamentos e mecanismos de persistência definidos para evitar processamento desnecessário.

**Requisitos relacionados:**
- RF007 — Consultar Cardápio
- RF011 — Criar Pedido
- RF012 — Consultar Pedido
- RF013 — Atualizar Status do Pedido
- RF015 — Processar Pagamento
- RN011 — Controle de estoque por unidade
- RN016 — Unidade do pedido

---

## RNF008 — Disponibilidade

**Descrição:**
O sistema deve manter as funcionalidades essenciais do fluxo de pedidos disponíveis durante o período de operação da rede, considerando as limitações do ambiente de execução do projeto.

**Categoria:**
Disponibilidade / Confiabilidade

**Critérios/garantias:**
- As funcionalidades essenciais de consulta de cardápio, criação de pedido, pagamento e acompanhamento de status devem permanecer disponíveis enquanto a aplicação e suas dependências estiverem operacionais.
- Falhas em uma operação específica não devem deixar dados de pedidos ou estoque em estado inconsistente.
- A indisponibilidade de uma dependência deve resultar em falha controlada da operação afetada.
- Após a recuperação das dependências necessárias, a aplicação deve poder retomar o processamento sem exigir reconstrução manual dos dados persistidos.
- Dados persistidos no banco de dados devem permanecer disponíveis após reinicializações da aplicação.

**Requisitos relacionados:**
- RF007 — Consultar Cardápio
- RF011 — Criar Pedido
- RF012 — Consultar Pedido
- RF013 — Atualizar Status do Pedido
- RF015 — Processar Pagamento
- RN020 — Baixa de estoque após pagamento aprovado
- RN026 — Ciclo de status do pedido

---

## RNF009 — Tolerância a Falhas no Pagamento

**Descrição:**
O sistema deve tratar falhas e resultados negativos durante o processamento de pagamentos sem comprometer a consistência do pedido ou do estoque.

**Categoria:**
Confiabilidade / Tolerância a Falhas

**Critérios/garantias:**
- Uma tentativa de pagamento recusada não deve causar baixa no estoque.
- O pedido deve permanecer em estado compatível com uma nova tentativa quando o pagamento não for aprovado.
- Cada tentativa de pagamento deve ter seu resultado registrado para fins de rastreabilidade.
- Uma falha durante o processamento não deve resultar em pagamento parcialmente aplicado ao pedido.
- Apenas um pagamento aprovado deve produzir os efeitos correspondentes sobre o pedido e o estoque.
- Uma nova tentativa de pagamento não deve provocar baixa duplicada de estoque.

**Requisitos relacionados:**
- RF015 — Processar Pagamento
- RF017 — Registrar Logs/Auditoria
- RN019 — Verificação de disponibilidade
- RN020 — Baixa de estoque após pagamento aprovado
- RN022 — Pagamento aprovado
- RN025 — Apenas um pagamento aprovado por pedido
- RN030 — Registro de eventos do pedido

---

## RNF010 — Documentação e Interoperabilidade da API

**Descrição:**
O sistema deve disponibilizar documentação técnica dos endpoints da API de forma padronizada, permitindo compreender e testar as operações expostas pelo backend.

**Categoria:**
Manutenibilidade / Interoperabilidade

**Critérios/garantias:**
- Os endpoints REST devem ser documentados utilizando o padrão OpenAPI.
- A documentação deve ser disponibilizada por meio de uma interface Swagger UI durante a execução da aplicação.
- A documentação deve permitir identificar os métodos HTTP e as rotas disponíveis.
- Os contratos de entrada e saída da API devem ser representados de forma compatível com os DTOs utilizados pela aplicação.
- A documentação deve auxiliar na identificação dos parâmetros necessários para execução das operações.
- A API deve utilizar formatos padronizados de comunicação HTTP e JSON nas operações implementadas.

**Requisitos relacionados:**
- RF001 — Autenticar Usuário
- RF003 — Cadastrar Cliente
- RF007 — Consultar Cardápio
- RF011 — Criar Pedido
- RF012 — Consultar Pedido
- RF013 — Atualizar Status do Pedido
- RF014 — Cancelar Pedido
- RF015 — Processar Pagamento