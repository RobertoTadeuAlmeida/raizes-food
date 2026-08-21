# Arquitetura do Sistema

O Raízes Food utiliza uma arquitetura em camadas (Layered Architecture),
separando as responsabilidades da aplicação em níveis distintos.

A organização adotada tem como objetivo manter o código simples,
facilitar a manutenção e evitar que regras de negócio fiquem misturadas
com acesso ao banco de dados ou tratamento de requisições HTTP.

## Camadas

### Controller

Responsável por receber as requisições da API e devolver as respostas.

O Controller não deve concentrar regras de negócio. Sua função principal é
receber os dados da requisição, encaminhá-los para a camada de serviço e
retornar o resultado adequado.

### Service

Responsável pelas regras de negócio e pelos fluxos principais do sistema.

É nessa camada que ficam decisões como:

- validar permissões;
- criar pedidos;
- processar pagamentos;
- atualizar estoque;
- cancelar pedidos;
- alterar o status de um pedido.

A camada de Service faz a ligação entre os Controllers, os Repositories
e serviços externos quando necessário.

### Repository

Responsável pelo acesso aos dados.

Os Repositories serão utilizados para consultar, salvar e atualizar as
entidades persistidas no banco de dados.

Essa camada será implementada utilizando Spring Data JPA.

### Banco de Dados

Responsável pela persistência das informações do sistema, como usuários,
unidades, produtos, estoque, pedidos e pagamentos.

## Integrações

O processamento de pagamento do MVP utiliza um Gateway de Pagamento Mock.

A comunicação com esse gateway deve ficar isolada da regra principal da
aplicação, permitindo que o Service apenas solicite o processamento e
receba o resultado como aprovado ou recusado.

Exemplo simplificado:

PedidoController
↓
PedidoService
↓
PaymentGateway
↓
MockPaymentGateway

## Organização geral

O fluxo principal entre as camadas segue a estrutura:

Controller
↓
Service
↓
Repository
↓
Banco de Dados

As entidades representam os dados do domínio, enquanto DTOs podem ser
utilizados para receber e devolver dados pela API sem expor diretamente
as entidades persistidas.

## Objetivo da arquitetura

A arquitetura em camadas foi escolhida por ser simples e adequada ao
escopo do projeto.

Ela permite separar responsabilidades sem introduzir padrões mais
complexos que não seriam necessários para o MVP.