package br.com.raizesfood.dto;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario
) {
}