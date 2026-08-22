package br.com.raizesfood.service;

import br.com.raizesfood.model.entity.Estoque;
import br.com.raizesfood.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public Estoque buscarEstoque(Long unidadeId, Long produtoId) {
        return estoqueRepository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Estoque não encontrado"));
    }

    public boolean possuiDisponibilidade(
            Long unidadeId,
            Long produtoId,
            int quantidade
    ) {
        if (quantidade <= 0) {
            return false;
        }

        Estoque estoque = buscarEstoque(unidadeId, produtoId);

        return estoque.getQuantidade() >= quantidade;
    }

    @Transactional
    public void adicionarEntrada(
            Long unidadeId,
            Long produtoId,
            int quantidade
    ) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de entrada deve ser maior que zero"
            );
        }

        Estoque estoque = buscarEstoque(unidadeId, produtoId);

        estoque.setQuantidade(
                estoque.getQuantidade() + quantidade
        );

        estoqueRepository.save(estoque);
    }

    @Transactional
    public void ajustarQuantidade(
            Long unidadeId,
            Long produtoId,
            int novaQuantidade
    ) {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException(
                    "A quantidade não pode ser negativa"
            );
        }

        Estoque estoque = buscarEstoque(unidadeId, produtoId);

        estoque.setQuantidade(novaQuantidade);

        estoqueRepository.save(estoque);
    }

    @Transactional
    public void baixarEstoque(
            Long unidadeId,
            Long produtoId,
            int quantidade
    ) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero"
            );
        }

        Estoque estoque = buscarEstoque(unidadeId, produtoId);

        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalStateException(
                    "Estoque insuficiente"
            );
        }

        estoque.setQuantidade(
                estoque.getQuantidade() - quantidade
        );

        estoqueRepository.save(estoque);
    }

    @Transactional
    public void retornarEstoque(
            Long unidadeId,
            Long produtoId,
            int quantidade
    ) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero"
            );
        }

        Estoque estoque = buscarEstoque(unidadeId, produtoId);

        estoque.setQuantidade(
                estoque.getQuantidade() + quantidade
        );

        estoqueRepository.save(estoque);
    }
}