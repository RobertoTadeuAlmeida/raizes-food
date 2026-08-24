package br.com.raizesfood.dto;

import br.com.raizesfood.model.enums.CanalPedido;

import java.util.List;

public record CriarPedidoRequest(
        Long unidadeId,
        Long clienteId,
        CanalPedido canalPedido,
        List<ItemPedidoRequest> itens
) {
}