package br.com.raizesfood.service;

import br.com.raizesfood.dto.ProdutoCardapioResponse;
import br.com.raizesfood.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioService {

    private final EstoqueRepository estoqueRepository;

    public CardapioService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public List<ProdutoCardapioResponse> buscarPorUnidade(Long unidadeId) {

        return estoqueRepository
                .findByUnidadeIdAndQuantidadeGreaterThan(unidadeId, 0)
                .stream()
                .filter(estoque -> estoque.getProduto().isAtivo())
                .map(estoque -> new ProdutoCardapioResponse(
                        estoque.getProduto().getId(),
                        estoque.getProduto().getNome(),
                        estoque.getProduto().getDescricao(),
                        estoque.getProduto().getPreco(),
                        estoque.getQuantidade()
                ))
                .toList();
    }
}