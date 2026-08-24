# Entendimento do Domínio

## 1. Contexto

O Raízes Food é um sistema de backend desenvolvido para a rede fictícia
de lanchonetes Raízes do Nordeste.

A rede possui diferentes unidades e necessita centralizar operações
relacionadas a usuários, cardápio, estoque, pedidos e pagamentos.

O sistema tem como objetivo disponibilizar uma API REST capaz de atender
os principais processos da rede, mantendo o controle de acesso por perfil,
a disponibilidade dos produtos por unidade e a rastreabilidade do ciclo
dos pedidos.

O MVP concentra-se principalmente no fluxo de pedido, desde sua criação
até a finalização, incluindo a verificação de disponibilidade, o
processamento simulado do pagamento, a movimentação do estoque e as
alterações de status.

## 2. Atores

### Cliente

Usuário que utiliza o sistema para realizar operações relacionadas aos
seus pedidos. Pode criar uma conta, autenticar-se, consultar o cardápio,
realizar e acompanhar pedidos e solicitar o perfil de Gerente.

### Gerente

Usuário responsável pela administração das unidades às quais estiver
vinculado. Pode realizar operações administrativas permitidas sobre suas
unidades, como gerenciamento de estoque e acompanhamento do ciclo dos
pedidos.

### Administrador

Usuário responsável pela administração geral da rede Raízes Food.
Possui acesso administrativo às unidades da rede, pode cadastrar unidades,
autorizar solicitações de perfil de Gerente e realizar operações
administrativas de abrangência geral.

### Atendente

Ator operacional relacionado aos pedidos realizados pelos canais de
balcão e totem. No escopo definido para o sistema, não possui perfil
de usuário autenticável nem permissões administrativas.

### Cozinha

Ator operacional relacionado à preparação dos pedidos. No MVP, não possui
autenticação própria. Sua participação no processo é representada pelo
avanço do pedido durante as etapas de preparação até que esteja pronto
para finalização.

### Gateway de Pagamento Mock

Serviço externo simulado responsável pelo processamento dos pagamentos
no MVP. O gateway retorna o resultado da tentativa como aprovado ou
recusado, permitindo validar o fluxo de pagamento sem integração com
um provedor financeiro real.

## 3. Processo Principal

O principal processo do sistema é o ciclo de um pedido.

Inicialmente, o pedido é criado para uma unidade e contém um ou mais
produtos. O sistema valida os produtos e verifica a disponibilidade das
quantidades solicitadas no estoque da unidade.

Quando o pedido é válido, ele é registrado com o status
`PENDENTE_PAGAMENTO`. O pagamento é então processado por meio do Gateway
de Pagamento Mock.

Caso o pagamento seja recusado, a tentativa é registrada e o pedido
permanece pendente, permitindo uma nova tentativa sem alteração do estoque.

Quando o pagamento é aprovado, a tentativa é registrada, os produtos são
baixados do estoque da unidade e o pedido pode seguir pelo ciclo:

`PENDENTE_PAGAMENTO`
→ `PAGAMENTO_APROVADO`
→ `EM_PREPARACAO`
→ `PRONTO`
→ `FINALIZADO`

O pedido também pode ser cancelado quando seu status e o perfil responsável
pela operação permitirem. Caso já tenha ocorrido baixa de estoque, as
quantidades correspondentes são devolvidas.

## 4. Conceitos Principais do Domínio

### Usuário

Representa uma conta autenticável. Os perfis existentes são Cliente,
Gerente e Administrador.

### Unidade

Representa uma unidade física da rede. Cada unidade possui seu próprio
controle de estoque e pode possuir um Gerente responsável.

### Produto e Cardápio

Os produtos fazem parte de um cardápio global compartilhado pela rede.
O preço do produto é único, enquanto sua disponibilidade depende do
estoque existente em cada unidade.

### Estoque

Representa a quantidade disponível de determinado produto em uma unidade.
A baixa ocorre após pagamento aprovado e pode haver retorno da quantidade
em caso de cancelamento aplicável.

### Pedido

Representa a solicitação de compra realizada para uma unidade. Possui
itens, canal de origem e status que representa sua situação atual.

### Pagamento

No MVP, o pagamento é representado por tentativas processadas por um
Gateway Mock. Um pedido pode possuir várias tentativas recusadas, mas
no máximo uma tentativa aprovada.

## 5. Limites do Sistema

O MVP do Raízes Food concentra-se nas funcionalidades necessárias para
demonstrar o fluxo principal do backend da rede.

Fazem parte do escopo:

- cadastro e autenticação de usuários;
- autorização de operações de acordo com o perfil;
- unidades e cardápio;
- controle de estoque por unidade;
- criação e consulta de pedidos;
- processamento de pagamento por Gateway Mock;
- cancelamento de pedidos;
- atualização do ciclo de status;
- baixa e retorno de estoque;
- rastreabilidade básica dos eventos do pedido;
- cuidados básicos relacionados à segurança e proteção dos dados.

Funcionalidades adicionais podem ser documentadas na análise do sistema
sem necessariamente fazer parte da implementação do MVP. Entre elas estão
recuperação de acesso, programa de fidelização e promoções.

O foco da implementação é demonstrar de forma consistente o fluxo crítico
de pedido, pagamento, estoque e atualização de status.