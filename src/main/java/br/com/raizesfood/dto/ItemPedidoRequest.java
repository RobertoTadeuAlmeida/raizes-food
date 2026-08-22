package br.com.raizesfood.dto;

public record ItemPedidoRequest(
        Long produtoId,
        Integer quantidade
) {
}