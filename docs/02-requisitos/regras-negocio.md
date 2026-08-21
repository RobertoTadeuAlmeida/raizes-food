# Regras de Negócio

## RN001 — E-mail único
O e-mail utilizado em uma conta deve ser único entre os usuários cadastrados no sistema.

**Regras:**
- Não deve existir mais de uma conta com o mesmo e-mail.
- O sistema deve verificar a existência do e-mail antes de criar uma nova conta.
- A regra se aplica às contas de Cliente, Gerente e Administrador.

---

## RN002 — Autorização por perfil
O acesso às funcionalidades do sistema deve ser controlado de acordo com o perfil do usuário autenticado.

**Perfis Autenticáveis:**
- Cliente
- Gerente
- Administrador

**Atores Operacionais (não autenticáveis):**
- Atendente — ator operacional utilizado em operações de balcão/totem; não é um perfil de login.
- Cozinha — resposta/processamento interno do sistema; não é um perfil de login.

**Regras:**
- Cliente acessa funcionalidades relacionadas à realização e acompanhamento de seus pedidos.
- Gerente acessa funcionalidades administrativas das unidades pelas quais é responsável.
- Administrador acessa funcionalidades administrativas de toda a rede Raízes Food.
- Funcionalidades protegidas não devem ser disponibilizadas sem a devida autorização.
- Atendente não recebe permissões administrativas; é um ator operacional em contextos específicos (BALCAO, TOTEM).
- Cozinha não é um usuário autenticado; o sistema registra alterações de status sem identificar funcionário específico.

---

## RN003 — Recuperação de acesso
O processo de recuperação de acesso deve validar a identidade do usuário antes de permitir a alteração da senha.

**Escopo:** **Fora do MVP / Feature futura.**

**Regras:**
- O usuário deve possuir uma conta cadastrada.
- A identidade deve ser validada antes da alteração da senha.
- A nova senha deve substituir a anterior após a conclusão.
- Quando implementada, estará disponível para Cliente, Gerente e Administrador.

---

## RN004 — Aprovação de Gerente
O perfil de Gerente deve ser concedido somente após autorização do Administrador responsável pela rede Raízes Food.

**Regras:**
- Todo usuário comum é inicialmente cadastrado com perfil de Cliente.
- Um usuário com perfil de Cliente pode solicitar o perfil de Gerente.
- So e permitido uma solicitação **PENDENTE** por Usuário.
- Apos **REJEITADA**, pode realizar nova solicitação.
- A solicitação de perfil é registrada separadamente do Usuário.
- A solicitação permanece **PENDENTE** até análise do Administrador.
- Enquanto a solicitação estiver **PENDENTE**, o Usuário permanece com perfil de Cliente.
- O Administrador pode aprovar ou rejeitar.
- Solicitação aprovada altera o perfil do Usuario de Cliente para Gerente.
- Solicitação rejeitada mantém o perfil de Cliente.
- O solicitante não define as unidades pelas quais será responsável.
- O Administrador define as unidades associadas ao Gerente na aprovação.
- Não existe perfil intermediário para solicitação pendente.

---

## RN005 — Administrador único da rede
A rede Raízes Food deve possuir exatamente um Usuario com perfil de Administrador responsável pela administração geral do sistema.

**Regras:**
- O Administrador administra toda a rede.
- Possui acesso às funcionalidades administrativas de todas as unidades.
- O sistema deve manter a existência de exatamente um Usuario com perfil de Administrador.
- Administrador permanece um perfil de Usuario, não uma entidade separada.
- A autoridade do Administrador sobre todas as unidades não cria relacionamento estrutural Administrador–Unidade.

---

## RN006 — Gerente responsável por unidades
Um Gerente pode ser responsável por uma ou várias unidades.

**Regras:**
- O vínculo com as unidades é definido pelo Administrador.
- O Gerente pode administrar as unidades às quais estiver vinculado.
- O Gerente não pode administrar unidades fora de sua responsabilidade.

---

## RN007 — Unidade com apenas um Gerente
Cada unidade deve possuir no máximo um Gerente responsável.

**Regras:**
- Uma unidade não pode possuir dois ou mais Gerentes responsáveis simultaneamente.
- Uma unidade pode permanecer sem Gerente até associação pelo Administrador.
- O sistema deve impedir associação que resulte em mais de um Gerente responsável.

---

## RN008 — Unidade inativa
Uma unidade desativada não deve estar disponível para novas operações, mas seu histórico deve ser preservado.

**Regras:**
- A desativação deve ser lógica (soft delete).
- O registro não deve ser excluído fisicamente.
- Unidade inativa não deve receber novos pedidos.
- Histórico relacionado deve permanecer preservado.
- Unidade pode ser reativada por usuário autorizado.

---

## RN009 — Cardápio único da rede
A rede Raízes Food deve possuir um único cardápio de produtos compartilhado por todas as unidades.

**Regras:**
- Produto cadastrado pertence ao cardápio global.
- Não deve existir cadastro independente do mesmo produto por unidade.
- Alterações nos dados globais refletem no cardápio da rede.
- O preço do produto é único para toda a rede.

---

## RN010 — Disponibilidade por unidade
A disponibilidade de um produto pode variar entre as unidades.

**Regras:**
- A disponibilidade é determinada pela quantidade existente no estoque da unidade.
- Um produto pode estar disponível em uma unidade e indisponível em outra.
- Indisponibilidade em uma unidade não remove o produto do cardápio global.
- Produto e disponibilidade são conceitos distintos.

---

## RN011 — Controle de estoque por unidade
Cada unidade deve possuir seu próprio controle de estoque.

**Regras:**
- O estoque registra a quantidade disponível dos produtos na unidade.
- A quantidade é independente entre unidades.
- O estoque fornece os dados necessários para verificar disponibilidade.
- O estoque não é o cadastro global do produto.

---

## RN012 — Permissão de gerenciamento do estoque
O gerenciamento do estoque deve respeitar o perfil e o vínculo com a unidade.

**Regras:**
- Administrador pode consultar e ajustar o estoque de qualquer unidade.
- Gerente pode consultar e ajustar somente unidades sob sua responsabilidade.
- Gerente não pode alterar estoque de unidade fora de sua responsabilidade.
- Usuários sem autorização não podem realizar operações administrativas de estoque.

---

## RN013 — Entrada de estoque
A entrada de estoque deve incrementar a quantidade disponível do produto na unidade.

**Regras:**
- Quantidade de entrada deve ser maior que zero.
- A entrada é somada à quantidade existente.
- A entrada não substitui a quantidade existente.
- A entrada ocorre para uma unidade específica.
- A operação respeita as permissões da RN012.

---

## RN014 — Produto inativo
Produtos desativados não devem estar disponíveis para novos pedidos, mas seu histórico deve ser preservado.

**Regras:**
- A desativação deve ser lógica.
- O registro não deve ser excluído fisicamente.
- Produto inativo não pode ser incluído em novos pedidos.
- Produto pode ser reativado por usuário autorizado.
- Histórico de pedidos que utilizaram o produto deve permanecer preservado.

---

## RN015 — Pedido deve possuir item
Todo pedido deve possuir pelo menos um item de produto.

**Regras:**
- Não é permitido criar pedido sem itens.
- Cada item identifica produto e quantidade.
- Quantidade deve ser válida.
- Um pedido pode possuir vários itens.

---

## RN016 — Unidade do pedido
Todo pedido deve estar associado a uma única unidade.

**Regras:**
- A unidade é definida durante a criação.
- A unidade deve estar ativa.
- Os itens utilizam a disponibilidade de estoque da unidade selecionada.
- A baixa ocorre no estoque da unidade associada.

---

## RN017 — Canal do pedido
Todo pedido deve registrar o canal pelo qual foi originado.

**Canais permitidos:**
- APP
- TOTEM
- BALCAO
- PICKUP
- WEB

**Regras:**
- O canal é representado conceitualmente como ENUM.
- Um pedido possui exatamente um canal de origem.
- O canal é registrado na criação.
- O canal é preservado para rastreabilidade.

---

## RN018 — Preço praticado no momento da compra
O ItemPedido deve preservar o preço praticado no momento da compra.

**Quando é capturado:** O preço é capturado durante a criação do pedido (RF011), não durante o pagamento.

**Regras:**
- O preço do produto é obtido no momento da criação do pedido.
- O preço vigente é copiado para o ItemPedido e preservado como histórico.
- O preço do pedido não depende de alterações futuras no produto.
- O preço praticado é registrado no ItemPedido e imutável.
- Alterações posteriores no preço do Produto não modificam o preço histórico de pedidos já criados.

**Regras relacionadas:**
- [RF011 — Criar Pedido]

---

## RN019 — Disponibilidade antes do pagamento
A disponibilidade dos itens na unidade deve ser verificada antes do prosseguimento para pagamento.

**Regras:**
- Produto deve estar ativo.
- Unidade deve possuir quantidade suficiente.
- Se algum item estiver indisponível ou insuficiente, o pedido não deve prosseguir normalmente para pagamento.
- A consulta não realiza baixa.
- A baixa ocorre somente após pagamento aprovado.

---

## RN020 — Baixa de estoque após pagamento aprovado
Os produtos do pedido devem ser retirados do estoque somente após aprovação do pagamento.

**Regras:**
- Pagamento deve estar aprovado antes da baixa.
- Quantidade baixada corresponde aos itens do pedido.
- A baixa ocorre no estoque da unidade do pedido.
- Pagamento recusado não baixa estoque.
- Criação do pedido não baixa estoque.

---

## RN021 — Gateway Mock
O processamento de pagamentos do MVP deve utilizar um Gateway de Pagamento Mock.

**Regras:**
- O gateway simula o processamento.
- Resultados possíveis: **APROVADO** ou **RECUSADO**.
- Não é necessária integração real no MVP.
- Resultado retornado deve ser registrado.

---

## RN022 — Pagamento aprovado
Um pagamento aprovado permite que o pedido avance para as etapas posteriores.

**Regras:**
- Tentativa é registrada como **APROVADA**.
- Pedido passa para **PAGAMENTO_APROVADO**.
- Aprovação permite baixa de estoque conforme RN020.
- Pedido pode seguir para **EM_PREPARACAO**.
- Um pedido não pode possuir mais de uma tentativa aprovada/efetivada.

---

## RN023 — Pagamento recusado
Um pagamento recusado não permite que o pedido avance para preparação.

**Regras:**
- Tentativa é registrada como **RECUSADA**.
- Pedido permanece em **PENDENTE_PAGAMENTO**.
- Estoque não é alterado.
- Recusa não cancela automaticamente o pedido.
- Nova tentativa pode ser realizada.

---

## RN024 — Nova tentativa de pagamento
Uma tentativa recusada não impede nova tentativa para o mesmo pedido.

**Regras:**
- Pedido pode possuir várias tentativas.
- Tentativas recusadas permanecem no histórico.
- Nova tentativa pode ocorrer enquanto o pedido estiver em **PENDENTE_PAGAMENTO**.
- Após aprovação, não deve existir novo pagamento aprovado para o mesmo pedido.

---

## RN025 — Apenas um pagamento aprovado por pedido
Cada pedido pode possuir no máximo uma tentativa de pagamento aprovada/efetivada.

**Regras:**
- Pedido pode possuir várias tentativas recusadas.
- Apenas uma tentativa pode ter resultado **APROVADO**.
- Após aprovação, novo processamento que resulte em outro pagamento aprovado deve ser impedido.
- Tentativas recusadas não são pagamentos efetivados.

---

## RN026 — Ciclo de status do pedido
O pedido deve seguir um ciclo de status definido.

**Fluxo principal:**

```text
PENDENTE_PAGAMENTO
        ↓
PAGAMENTO_APROVADO
        ↓
EM_PREPARACAO
        ↓
PRONTO
        ↓
FINALIZADO
```

**Status permitidos:**
- PENDENTE_PAGAMENTO
- PAGAMENTO_APROVADO
- EM_PREPARACAO
- PRONTO
- FINALIZADO
- CANCELADO

**Regras:**
- Pedido recém-criado inicia em **PENDENTE_PAGAMENTO**.
- Só avança para **PAGAMENTO_APROVADO** após pagamento aprovado.
- **CANCELADO** é interrupção válida do ciclo.
- Cozinha é processamento/resposta interna, não usuário autenticado.
- Sistema preserva o status atual.

---

## RN027 — Cancelamento por perfil
O direito de cancelar depende do perfil.

**Regras:**
- Cliente pode cancelar nas situações permitidas pela RN028.
- Gerente pode cancelar nas situações permitidas pela RN028.
- Administrador pode cancelar nas situações permitidas pela RN028.
- Usuários sem permissão não podem cancelar.

---

## RN028 — Cancelamento por status
O cancelamento deve respeitar a situação atual e o perfil.

| Situação | Cliente | Gerente | Administrador |
| --- | --- | --- | --- |
| PENDENTE_PAGAMENTO | SIM | SIM | SIM |
| PAGAMENTO_APROVADO | SIM | SIM | SIM |
| EM_PREPARACAO | NÃO | SIM | SIM |
| PRONTO | NÃO | NÃO | NÃO |
| FINALIZADO | NÃO | NÃO | NÃO |

**Regras:**
- Pedido cancelado assume **CANCELADO**.
- Cliente não pode cancelar pedido em preparação.
- Gerente e Administrador podem cancelar em **EM_PREPARACAO**.
- Pedidos em **PRONTO** ou **FINALIZADO** não podem ser cancelados.

---

## RN029 — Retorno ao estoque após cancelamento
Quando um pedido cancelado já tiver causado baixa no estoque, as quantidades correspondentes devem retornar ao estoque da unidade.

**Regras:**
- Retorno ocorre quando já houve baixa.
- Quantidade devolvida corresponde aos itens do pedido.
- Retorno ocorre no estoque da unidade do pedido.
- Cancelamento em **PENDENTE_PAGAMENTO** não gera retorno.
- Retorno deve preservar a consistência da quantidade disponível.

---

## RN030 — Registro de eventos do pedido
O sistema deve registrar eventos relevantes do ciclo do pedido para permitir rastreabilidade.

**Eventos mínimos:**
- criação do pedido;
- tentativa e resultado de pagamento;
- alteração de status;
- cancelamento.

**Regras:**
- Eventos devem estar associados ao pedido.
- Tentativas recusadas também permanecem no histórico.
- Alterações relevantes do ciclo devem ser registradas.
- O histórico deve permitir identificar a sequência de eventos.
- O objetivo é rastreabilidade, sem criar sistema complexo de observabilidade.
- Não é necessário identificar funcionário em operações sem autenticação (BALCAO, TOTEM, COZINHA).
- O sistema registra a sequência de eventos do pedido independentemente de quem realizou a ação em contextos operacionais.