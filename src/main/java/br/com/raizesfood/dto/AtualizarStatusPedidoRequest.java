package br.com.raizesfood.dto;

import br.com.raizesfood.model.enums.StatusPedido;

public record AtualizarStatusPedidoRequest(
        StatusPedido status
) {
}