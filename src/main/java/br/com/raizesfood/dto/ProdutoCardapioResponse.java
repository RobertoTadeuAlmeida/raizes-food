package br.com.raizesfood.dto;

import java.math.BigDecimal;

public record ProdutoCardapioResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidadeDisponivel
) {
}