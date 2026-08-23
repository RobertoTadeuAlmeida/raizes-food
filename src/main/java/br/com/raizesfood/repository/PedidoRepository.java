package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.model.enums.CanalPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    List<Pedido> findByCanal(CanalPedido canal);
}
