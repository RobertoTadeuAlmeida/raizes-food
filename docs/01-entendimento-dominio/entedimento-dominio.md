# Entendimento do Domínio

## 1. Contexto

Breve descrição da rede Raízes Food, problema que o sistema resolve
e objetivo do backend.

## 2. Atores

- Cliente
- Gerente
- Administrador
- Atendente
- Cozinha
- Gateway de Pagamento Mock

Explicação curta da responsabilidade de cada um.

## 3. Processo Principal

Descrição resumida do fluxo:

Pedido
→ seleção da unidade e produtos
→ verificação de disponibilidade
→ pagamento
→ baixa de estoque
→ preparação
→ finalização

## 4. Limites do Sistema


O MVP do Raízes Food concentra-se nas funcionalidades principais do backend
da rede, incluindo autenticação, unidades, produtos, estoque, pedidos,
pagamento simulado e rastreabilidade básica.

Fazem parte do escopo do MVP:
- autenticação e autorização por perfil;
- gerenciamento de unidades;
- cardápio e produtos;
- estoque por unidade;
- criação, consulta e cancelamento de pedidos;
- processamento de pagamento por Gateway Mock;
- atualização do ciclo do pedido;
- cuidados mínimos de segurança e LGPD;
- logs básicos das operações relevantes.

Funcionalidades documentadas ou previstas, mas não priorizadas para o MVP,
devem ser identificadas explicitamente como futuras.