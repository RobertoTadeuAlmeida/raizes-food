package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.EventoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoPedidoRepository extends JpaRepository<EventoPedido, Long> {
}
