package br.com.raizesfood.gateway;

import br.com.raizesfood.model.enums.StatusPagamento;

public interface GatewayPagamento {

    StatusPagamento processar();
}