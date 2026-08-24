# Requisitos Funcionais

## RF001 — Autenticar usuário

**Descrição**

Permitir que usuários autenticáveis acessem o sistema com e-mail e senha.

**Atores**

- Cliente
- Gerente
- Administrador

**Pré-condições**

- O usuário deve possuir uma conta cadastrada.

**Fluxo principal**

1. O usuário informa e-mail e senha.
2. O sistema valida as credenciais e identifica o perfil do usuário.
3. O sistema autentica o usuário e disponibiliza as funcionalidades autorizadas para seu perfil.

**Fluxos alternativos / exceções**

- Caso as credenciais sejam inválidas, o sistema rejeita a autenticação e mantém o usuário sem acesso às funcionalidades protegidas.

**Pós-condições**

- O usuário autenticado pode acessar as funcionalidades autorizadas para seu perfil.

**Regras relacionadas**

- RN002 — Autorização por perfil

---

## RF002 — Recuperar acesso

**Descrição**

Permitir que um usuário autenticável recupere o acesso à sua conta. **Fora do MVP / Feature futura.**

**Atores**

- Cliente
- Gerente
- Administrador

**Pré-condições**

- O usuário deve possuir uma conta cadastrada.
- O usuário não deve possuir acesso à senha atual.

**Fluxo principal**

1. O usuário solicita a recuperação de acesso.
2. O sistema valida a identidade do usuário.
3. O usuário define uma nova senha.
4. O sistema substitui a senha anterior pela nova senha.

**Fluxos alternativos / exceções**

- Caso a identidade não seja validada, o sistema impede a alteração da senha.
- Caso os dados fornecidos sejam inválidos, o sistema informa que não foi possível recuperar o acesso.

**Pós-condições**

- A nova senha permite a autenticação e a senha anterior deixa de ser válida.

**Regras relacionadas**

- RN003 — Recuperação de acesso

---

## RF003 — Cadastrar Cliente

**Descrição**

Permitir que uma pessoa crie uma conta de Cliente para realizar e acompanhar pedidos.

**Atores**

- Cliente

**Pré-condições**

- Não deve existir conta cadastrada com o e-mail informado.

**Fluxo principal**

1. O Cliente informa os dados necessários para o cadastro, incluindo nome, e-mail, telefone, senha e confirmação da senha.
2. O sistema valida os dados, a confirmação da senha e a unicidade do e-mail.
3. O sistema cria a conta com perfil de Cliente.

**Fluxos alternativos / exceções**

- Caso algum dado obrigatório seja inválido ou não informado, o sistema impede o cadastro e indica a correção necessária.
- Caso o e-mail já esteja cadastrado, o sistema impede a criação da conta.
- Caso a confirmação seja diferente da senha informada, o sistema impede o cadastro e solicita a correção.

**Pós-condições**

- Uma conta com perfil de Cliente e credenciais de autenticação é criada.
- Os dados pessoais coletados ficam sujeitos às regras de privacidade e LGPD definidas para o sistema.

**Regras relacionadas**

- RN001 — E-mail único
- RN002 — Autorização por perfil

---

## RF004 — Solicitar e autorizar perfil de Gerente

**Descrição**

Permitir que um Cliente solicite o perfil de Gerente e que o Administrador aprove ou rejeite a solicitação, definindo as unidades do Gerente quando houver aprovação.

**Atores**

- Cliente
- Administrador

**Pré-condições**

- O solicitante deve possuir uma conta com perfil de Cliente.
- Deve existir um Administrador responsável pela rede.

**Fluxo principal**

1. O Cliente solicita o perfil de Gerente.
2. O sistema registra a solicitação com status `PENDENTE`, sem alterar o perfil de Cliente.
3. O Administrador analisa e aprova a solicitação.
4. O Administrador define as unidades pelas quais o Gerente será responsável.
5. O sistema altera o perfil do usuário para Gerente e libera as funcionalidades correspondentes.

**Fluxos alternativos / exceções**

- Caso o Administrador rejeite a solicitação, o sistema registra o status `REJEITADA`, mantém o perfil de Cliente e permite uma nova solicitação.
- Enquanto existir uma solicitação `PENDENTE`, o usuário permanece como Cliente, não acessa funcionalidades de Gerente e não pode criar outra solicitação.

**Pós-condições**

- Em caso de aprovação, a solicitação fica `APROVADA`, o usuário passa a Gerente e fica associado às unidades definidas pelo Administrador.
- Em caso de rejeição, a solicitação fica `REJEITADA` e o usuário permanece como Cliente.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN004 — Aprovação de Gerente
- RN005 — Administrador único da rede
- RN006 — Gerente responsável por unidades
- RN007 — Unidade com apenas um Gerente

---

## RF005 — Cadastrar Unidade

**Descrição**

Permitir que o Administrador cadastre novas unidades da rede Raízes Food.

**Atores**

- Administrador

**Pré-condições**

- O Administrador deve estar autenticado.

**Fluxo principal**

1. O Administrador informa o nome, o endereço e os dados de contato da nova unidade.
2. O sistema valida os dados informados.
3. O sistema registra a unidade e a disponibiliza para posterior associação a um Gerente.

**Fluxos alternativos / exceções**

- Caso algum dado obrigatório seja inválido ou não informado, o sistema impede o cadastro e indica a correção necessária.

**Pós-condições**

- Uma nova unidade sem Gerente responsável fica registrada e disponível para associação posterior.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN007 — Unidade com apenas um Gerente

---

## RF006 — Gerenciar Unidade

**Descrição**

Permitir que o Gerente gerencie as unidades sob sua responsabilidade e que o Administrador gerencie qualquer unidade da rede.

**Atores**

- Gerente
- Administrador

**Pré-condições**

- O usuário deve estar autenticado.
- Para operações do Gerente, a unidade deve estar sob sua responsabilidade.

**Fluxo principal**

1. O usuário solicita a consulta de unidades.
2. O sistema apresenta as unidades sob responsabilidade do Gerente ou todas as unidades para o Administrador.
3. O usuário consulta ou seleciona uma unidade para alterar seus dados.
4. O sistema valida a permissão e os dados informados e atualiza a unidade.

**Fluxos alternativos / exceções**

- Caso o usuário solicite a desativação, o sistema marca a unidade como inativa, preserva seu histórico e impede novas operações.
- Caso o usuário solicite a reativação, o sistema marca a unidade como ativa e volta a permitir operações normais.
- Caso o Gerente tente gerenciar uma unidade fora de sua responsabilidade, o sistema impede a ação.
- Caso os novos dados sejam inválidos, o sistema impede a atualização e indica a correção necessária.

**Pós-condições**

- A unidade reflete os dados e o estado ativo ou inativo resultantes da operação, sem exclusão física.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN006 — Gerente responsável por unidades
- RN008 — Unidade inativa

---

## RF007 — Cadastrar Produto

**Descrição**

Permitir que o Administrador e o Gerente cadastrem produtos no cardápio global da rede.

**Atores**

- Gerente
- Administrador

**Pré-condições**

- O usuário deve estar autenticado como Gerente ou Administrador.

**Fluxo principal**

1. O usuário informa nome, descrição e preço do produto.
2. O sistema valida os dados informados.
3. O sistema registra o produto no cardápio global para posterior controle de disponibilidade por unidade.

**Fluxos alternativos / exceções**

- Caso algum dado obrigatório seja inválido ou não informado, o sistema impede o cadastro e indica a correção necessária.
- Caso o produto já esteja cadastrado, o sistema impede a duplicidade.

**Pós-condições**

- Um novo produto, com preço único para toda a rede, fica registrado no cardápio global.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN009 — Cardápio único da rede
- RN010 — Disponibilidade por unidade
- RN014 — Produto inativo

---

## RF008 — Gerenciar Produto

**Descrição**

Permitir que o Administrador e o Gerente consultem, alterem, desativem e reativem produtos do cardápio global.

**Atores**

- Gerente
- Administrador

**Pré-condições**

- O usuário deve estar autenticado como Gerente ou Administrador.
- O produto deve estar cadastrado.

**Fluxo principal**

1. O usuário consulta os produtos e seus estados ativo ou inativo.
2. O usuário seleciona um produto e altera nome, descrição ou preço.
3. O sistema valida os dados e atualiza o produto no cardápio global.

**Fluxos alternativos / exceções**

- Caso o usuário desative o produto, o sistema o marca como inativo, preserva seu histórico e impede sua inclusão em novos pedidos.
- Caso o usuário reative o produto, o sistema o marca como ativo, sujeito à disponibilidade de estoque em cada unidade.
- Caso os novos dados sejam inválidos, o sistema impede a atualização e indica a correção necessária.

**Pós-condições**

- O produto reflete os dados e o estado ativo ou inativo resultantes da operação, sem exclusão física.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN009 — Cardápio único da rede
- RN010 — Disponibilidade por unidade
- RN014 — Produto inativo

---

## RF009 — Gerenciar Estoque

**Descrição**

Permitir que o Administrador e o Gerente consultem e ajustem manualmente o estoque de produtos por unidade.

**Atores**

- Gerente
- Administrador

**Pré-condições**

- O usuário deve estar autenticado.
- Para operações do Gerente, a unidade deve estar sob sua responsabilidade.
- O produto deve estar cadastrado no cardápio global.

**Fluxo principal**

1. O usuário solicita a consulta do estoque de uma unidade.
2. O sistema valida a permissão e apresenta a quantidade disponível de cada produto na unidade.
3. O usuário seleciona um produto e informa a nova quantidade ou o ajuste a aplicar.
4. O sistema valida o valor e atualiza o estoque do produto na unidade.

**Fluxos alternativos / exceções**

- Caso o Gerente tente consultar ou ajustar o estoque de outra unidade, o sistema impede a ação.
- Caso a quantidade informada seja inválida, o sistema impede o ajuste.

**Pós-condições**

- O estoque do produto na unidade reflete o ajuste realizado.

**Regras relacionadas**

- RN010 — Disponibilidade por unidade
- RN011 — Controle de estoque por unidade
- RN012 — Permissão de gerenciamento do estoque

---

## RF010 — Controlar disponibilidade e entrada de estoque

**Descrição**

Permitir o registro de entrada de estoque e determinar a disponibilidade de um produto em cada unidade.

**Atores**

- Gerente
- Administrador

**Pré-condições**

- O usuário deve estar autenticado.
- Para operações do Gerente, a unidade deve estar sob sua responsabilidade.
- O produto deve estar cadastrado e ativo no cardápio global.

**Fluxo principal**

1. O usuário seleciona a unidade e o produto e informa a quantidade recebida.
2. O sistema valida os dados e soma a entrada à quantidade existente no estoque da unidade.
3. O sistema atualiza a disponibilidade do produto na unidade.

**Fluxos alternativos / exceções**

- Quando consultada, a disponibilidade é informada conforme a quantidade em estoque do produto na unidade.
- Caso a quantidade de entrada seja zero ou negativa, o sistema impede o registro.
- Caso o Gerente tente registrar uma entrada em outra unidade, o sistema impede a ação.

**Pós-condições**

- A quantidade em estoque é incrementada e a disponibilidade reflete o novo valor.

**Regras relacionadas**

- RN010 — Disponibilidade por unidade
- RN011 — Controle de estoque por unidade
- RN012 — Permissão de gerenciamento do estoque
- RN013 — Entrada de estoque

---

## RF011 — Criar Pedido

**Descrição**

Permitir a criação de um Pedido por Cliente ou em contexto operacional, com unidade, canal e um ou mais ItemPedido.

**Atores**

- Cliente
- Atendente

**Pré-condições**

- Quando a criação for iniciada pelo Cliente, ele deve estar autenticado.
- A unidade selecionada deve estar ativa.
- Os produtos selecionados devem estar cadastrados e ativos no cardápio global.

**Fluxo principal**

1. O Cliente ou o contexto operacional inicia a criação do Pedido e seleciona a unidade.
2. O ator informa o canal `APP`, `TOTEM`, `BALCAO`, `PICKUP` ou `WEB` e adiciona um ou mais produtos com suas quantidades.
3. O sistema valida os produtos e verifica a disponibilidade na unidade, sem realizar baixa de estoque.
4. O sistema registra em cada ItemPedido o preço vigente do Produto.
5. O sistema cria o Pedido com identificação própria e status `PENDENTE_PAGAMENTO`.

**Fluxos alternativos / exceções**

- Caso um produto esteja inativo ou indisponível na unidade, o sistema impede sua inclusão e informa a indisponibilidade.
- Caso o Pedido não possua ao menos um ItemPedido, o sistema impede sua criação.
- Caso a unidade esteja inativa, o sistema impede a criação do Pedido.

**Pós-condições**

- O Pedido fica associado a exatamente uma Unidade, ao canal informado e a um ou mais ItemPedido com preços históricos preservados.
- Quando houver Cliente autenticado, o Pedido fica associado a ele; nos contextos `BALCAO` e `TOTEM`, o Pedido pode não possuir Cliente.
- O Atendente permanece ator operacional não autenticável e não fica associado ao Pedido como Usuario.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN014 — Produto inativo
- RN015 — Pedido deve possuir item
- RN016 — Unidade do pedido
- RN017 — Canal do pedido
- RN018 — Preço praticado no momento da compra
- RN019 — Disponibilidade antes do pagamento
- RN030 — Registro de eventos do pedido

---

## RF012 — Consultar e acompanhar Pedido

**Descrição**

Permitir a consulta de Pedidos e de seus status conforme o perfil autenticado ou o contexto operacional autorizado.

**Atores**

- Cliente
- Atendente
- Gerente
- Administrador

**Pré-condições**

- Cliente, Gerente e Administrador devem estar autenticados.
- Para consulta operacional, deve ser informado o identificador de um Pedido existente.

**Fluxo principal**

1. O usuário autenticado solicita a consulta de Pedidos.
2. O sistema apresenta somente os Pedidos do Cliente, das unidades do Gerente ou de toda a rede para o Administrador.
3. O usuário seleciona um Pedido.
4. O sistema apresenta identificação, unidade, canal, ItemPedido e status atual.

**Fluxos alternativos / exceções**

- Em `BALCAO` ou `TOTEM`, o Atendente consulta sem autenticação somente o Pedido correspondente ao identificador informado, sem acesso a uma listagem geral.
- Caso o usuário tente consultar um Pedido fora de seu escopo de acesso, o sistema impede a visualização.

**Pós-condições**

- O ator visualiza somente os Pedidos permitidos para seu perfil ou o Pedido específico autorizado no contexto operacional.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN017 — Canal do pedido
- RN026 — Ciclo de status do pedido
- RN030 — Registro de eventos do pedido

---

## RF013 — Atualizar status do Pedido

**Descrição**

Controlar o ciclo de vida do Pedido por meio de mudanças de status até sua finalização ou cancelamento.

**Atores**

- Sistema

**Pré-condições**

- O Pedido deve estar registrado.
- A transição deve ser permitida pelo ciclo de status do Pedido.

**Fluxo principal**

1. O Pedido é criado com status `PENDENTE_PAGAMENTO`.
2. Após a aprovação do pagamento, o sistema altera o status para `PAGAMENTO_APROVADO`.
3. Ao iniciar a preparação, o sistema altera o status para `EM_PREPARACAO`.
4. Após concluir a preparação, o sistema altera o status para `PRONTO`.
5. Após a entrega ou retirada, o sistema altera o status para `FINALIZADO`.

**Fluxos alternativos / exceções**

- Caso o pagamento seja recusado, o Pedido permanece em `PENDENTE_PAGAMENTO`.
- O status `CANCELADO` interrompe validamente o fluxo conforme o RF014.
- A Cozinha participa como processo interno, sem autenticação individual.

**Pós-condições**

- O Pedido preserva o status atual e o evento da alteração para rastreabilidade.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN026 — Ciclo de status do pedido
- RN030 — Registro de eventos do pedido

---

## RF014 — Cancelar Pedido

**Descrição**

Permitir que Cliente, Gerente e Administrador cancelem um Pedido conforme seu perfil e o status atual.

**Atores**

- Cliente
- Gerente
- Administrador

**Pré-condições**

- O usuário deve estar autenticado.
- O Pedido deve estar registrado.

**Fluxo principal**

1. O usuário solicita o cancelamento do Pedido.
2. O sistema verifica o status do Pedido e a permissão do perfil conforme a tabela de cancelamento.
3. O sistema altera o status do Pedido para `CANCELADO`.
4. Se já houve baixa, o sistema retorna as quantidades dos ItemPedido ao estoque da Unidade.
5. O sistema registra o cancelamento para rastreabilidade.

**Fluxos alternativos / exceções**

- Caso o perfil não possa cancelar o Pedido no status atual, o sistema impede a ação e informa o motivo.
- O cancelamento em `PENDENTE_PAGAMENTO` não altera o estoque, pois ainda não houve baixa.

| Status | Cliente | Gerente | Administrador |
| --- | --- | --- | --- |
| PENDENTE_PAGAMENTO | SIM | SIM | SIM |
| PAGAMENTO_APROVADO | SIM | SIM | SIM |
| EM_PREPARACAO | NÃO | SIM | SIM |
| PRONTO | NÃO | NÃO | NÃO |
| FINALIZADO | NÃO | NÃO | NÃO |

**Pós-condições**

- O Pedido fica com status `CANCELADO` e, quando já tiver ocorrido baixa, o estoque da Unidade recebe o retorno correspondente.

**Regras relacionadas**

- RN027 — Cancelamento por perfil
- RN028 — Cancelamento por status
- RN029 — Retorno ao estoque após cancelamento
- RN030 — Registro de eventos do pedido

---

## RF015 — Processar Pagamento

**Descrição**

Processar o pagamento de um Pedido por meio do Gateway de Pagamento Mock antes do início da preparação.

**Atores**

- Cliente
- Atendente

**Pré-condições**

- O Pedido deve estar com status `PENDENTE_PAGAMENTO`.
- Não deve existir tentativa de pagamento aprovada para o Pedido.

**Fluxo principal**

1. O Cliente ou Atendente solicita o pagamento do Pedido.
2. O sistema envia a solicitação ao Gateway de Pagamento Mock.
3. O Gateway de Pagamento Mock retorna o resultado `APROVADO`.
4. O sistema registra a tentativa como `APROVADA`.
5. O sistema altera o status do Pedido para `PAGAMENTO_APROVADO`.
6. O sistema realiza a baixa dos ItemPedido no estoque da Unidade correspondente.
7. O Pedido segue para a preparação.

**Fluxos alternativos / exceções**

- Se o resultado for `RECUSADO`, o sistema registra a tentativa como `RECUSADA`, mantém o Pedido em `PENDENTE_PAGAMENTO`, não altera o estoque e permite nova tentativa.
- Caso já exista uma tentativa `APROVADA`, o sistema impede novo processamento do pagamento.

**Pós-condições**

- A tentativa fica registrada como `APROVADA` ou `RECUSADA`.
- Quando aprovada, o Pedido fica em `PAGAMENTO_APROVADO`, o estoque sofre a baixa e o Pedido pode seguir para preparação.
- Quando recusada, o Pedido permanece em `PENDENTE_PAGAMENTO` e o estoque permanece inalterado.

**Regras relacionadas**

- RN019 — Disponibilidade antes do pagamento
- RN020 — Baixa de estoque após pagamento aprovado
- RN021 — Gateway Mock
- RN022 — Pagamento aprovado
- RN023 — Pagamento recusado
- RN024 — Nova tentativa de pagamento
- RN025 — Apenas um pagamento aprovado por pedido
- RN026 — Ciclo de status do pedido
- RN030 — Registro de eventos do pedido

---

## RF016 — Registrar Pagamento

**Descrição**

Registrar as tentativas de pagamento de um Pedido, incluindo múltiplas recusas e no máximo uma aprovação.

**Atores**

- Sistema

**Pré-condições**

- Uma tentativa deve ter sido processada pelo Gateway de Pagamento Mock.

**Fluxo principal**

1. O sistema recebe o resultado da tentativa processada.
2. O sistema associa a tentativa ao Pedido e registra o resultado `APROVADO` ou `RECUSADO`.
3. O sistema disponibiliza o resultado no histórico do Pedido.

**Fluxos alternativos / exceções**

- Caso já exista uma tentativa `APROVADA`, o sistema impede o registro de outra aprovação.
- Tentativas `RECUSADAS` podem ser registradas enquanto não houver tentativa aprovada.

**Pós-condições**

- A tentativa fica registrada com seu resultado e associada ao Pedido.
- Tentativas recusadas permanecem no histórico; uma tentativa aprovada representa o pagamento efetivado.

**Regras relacionadas**

- RN021 — Gateway Mock
- RN022 — Pagamento aprovado
- RN023 — Pagamento recusado
- RN024 — Nova tentativa de pagamento
- RN025 — Apenas um pagamento aprovado por pedido
- RN030 — Registro de eventos do pedido

---

## RF017 — Registrar logs e auditoria

**Descrição**

Registrar eventos relevantes do ciclo do Pedido para permitir sua rastreabilidade.

**Atores**

- Sistema

**Pré-condições**

- Deve ocorrer criação, tentativa ou resultado de pagamento, alteração de status ou cancelamento de um Pedido.

**Fluxo principal**

1. Um evento relevante ocorre no ciclo do Pedido.
2. O sistema registra o evento e o associa ao Pedido.
3. O sistema mantém o registro disponível para consulta de rastreabilidade.

**Fluxos alternativos / exceções**

- Em operações sem autenticação nos contextos `BALCAO`, `TOTEM` ou `COZINHA`, o evento é registrado sem identificação individual de funcionário.

**Pós-condições**

- O histórico do Pedido preserva a sequência dos eventos relevantes para rastreabilidade.

**Regras relacionadas**

- RN030 — Registro de eventos do pedido

---

## RF018 — Gerenciar Fidelização

**Descrição**

Permitir que Clientes participem de um programa de fidelização da rede Raízes Food, acumulando benefícios a partir de pedidos elegíveis e utilizando-os conforme as regras definidas pela rede. **Fora do MVP / Feature futura.**

**Atores**

- Cliente
- Administrador

**Pré-condições**

- O Cliente deve possuir uma conta cadastrada.
- Para consultar ou utilizar benefícios, o Cliente deve estar autenticado.
- O Pedido utilizado para geração de benefícios deve estar associado ao Cliente.

**Fluxo principal**

1. O Cliente realiza um Pedido elegível para o programa de fidelização.
2. Após a finalização do Pedido, o sistema calcula o benefício correspondente conforme as regras vigentes.
3. O sistema associa o benefício ao Cliente.
4. O Cliente consulta o saldo ou os benefícios disponíveis.
5. Quando permitido pelas regras vigentes, o Cliente utiliza o benefício em um novo Pedido.

**Fluxos alternativos / exceções**

- Pedidos cancelados não geram benefícios de fidelização.
- Pedidos sem Cliente identificado, como pedidos realizados sem identificação nos contextos `BALCAO` ou `TOTEM`, não geram benefícios associados a uma conta.
- Caso o Cliente não possua saldo ou benefício suficiente, o sistema impede sua utilização.
- Benefícios expirados ou indisponíveis não podem ser utilizados.
- O Administrador pode definir ou alterar as regras do programa de fidelização para utilizações futuras.

**Pós-condições**

- Quando elegível, o benefício gerado fica associado ao Cliente.
- Quando utilizado, o saldo ou benefício correspondente é atualizado.
- O histórico de benefícios permanece associado à conta do Cliente para fins de consulta e rastreabilidade.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN016 — Unidade do pedido
- RN026 — Ciclo de status do pedido
- RN030 — Registro de eventos do pedido

---

## RF019 — Gerenciar Promoções e Campanhas

**Descrição**

Permitir que a rede Raízes Food defina promoções e campanhas aplicáveis aos produtos e pedidos conforme período e critérios previamente estabelecidos. **Fora do MVP / Feature futura.**

**Atores**

- Administrador
- Cliente

**Pré-condições**

- O Administrador deve estar autenticado para cadastrar ou alterar promoções e campanhas.
- Os produtos relacionados a uma promoção devem estar cadastrados no cardápio global.
- Para utilização de uma promoção, ela deve estar ativa e dentro do período de validade.

**Fluxo principal**

1. O Administrador cadastra uma promoção ou campanha.
2. O Administrador informa as condições de aplicação, os produtos envolvidos e o período de validade.
3. O sistema valida os dados e registra a promoção.
4. Durante a criação de um Pedido, o sistema verifica as promoções aplicáveis.
5. Quando os critérios forem atendidos, o sistema aplica o benefício correspondente ao Pedido.
6. O Cliente visualiza o benefício aplicado antes da conclusão da compra.

**Fluxos alternativos / exceções**

- Caso a promoção esteja fora do período de validade, o sistema não aplica o benefício.
- Caso o Pedido não atenda aos critérios definidos, a promoção não é aplicada.
- Produtos inativos ou indisponíveis na unidade não podem ser incluídos no Pedido apenas por estarem associados a uma promoção.
- Caso uma promoção seja desativada pelo Administrador, ela deixa de ser considerada em novos Pedidos.
- Alterações em uma promoção não devem modificar os valores históricos de Pedidos já realizados.

**Pós-condições**

- Promoções válidas ficam disponíveis durante o período e nas condições definidas.
- Quando aplicável, o Pedido preserva os valores praticados no momento da compra.
- Pedidos anteriores permanecem com seus valores históricos mesmo após alteração ou encerramento da promoção.

**Regras relacionadas**

- RN002 — Autorização por perfil
- RN009 — Cardápio único da rede
- RN014 — Produto inativo
- RN016 — Unidade do pedido
- RN018 — Preço praticado no momento da compra

---