package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByUnidadeIdAndProdutoId(
            Long unidadeId,
            Long produtoId
    );
}