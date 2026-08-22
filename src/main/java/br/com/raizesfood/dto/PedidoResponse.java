package br.com.raizesfood.dto;

import br.com.raizesfood.model.enums.CanalPedido;
import br.com.raizesfood.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long unidadeId,
        Long clienteId,
        CanalPedido canal,
        StatusPedido status,
        LocalDateTime criadoEm,
        List<ItemPedidoResponse> itens
) {
}