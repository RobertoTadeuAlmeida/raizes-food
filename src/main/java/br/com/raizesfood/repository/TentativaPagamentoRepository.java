package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.TentativaPagamento;
import br.com.raizesfood.model.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TentativaPagamentoRepository extends JpaRepository<TentativaPagamento, Long> {

    boolean existsByPedidoIdAndResultado(
            Long pedidoId,
            StatusPagamento resultado
    );}
