package br.com.raizesfood.service;

import br.com.raizesfood.gateway.GatewayPagamento;
import br.com.raizesfood.model.entity.EventoPedido;
import br.com.raizesfood.model.entity.ItemPedido;
import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.model.entity.TentativaPagamento;
import br.com.raizesfood.model.enums.StatusPagamento;
import br.com.raizesfood.model.enums.StatusPedido;
import br.com.raizesfood.repository.EventoPedidoRepository;
import br.com.raizesfood.repository.PedidoRepository;
import br.com.raizesfood.repository.TentativaPagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoService {

    private final PedidoRepository pedidoRepository;
    private final TentativaPagamentoRepository tentativaPagamentoRepository;
    private final EventoPedidoRepository eventoPedidoRepository;
    private final EstoqueService estoqueService;
    private final GatewayPagamento gatewayPagamento;

    public PagamentoService(
            PedidoRepository pedidoRepository,
            TentativaPagamentoRepository tentativaPagamentoRepository,
            EventoPedidoRepository eventoPedidoRepository,
            EstoqueService estoqueService,
            GatewayPagamento gatewayPagamento
    ) {
        this.pedidoRepository = pedidoRepository;
        this.tentativaPagamentoRepository = tentativaPagamentoRepository;
        this.eventoPedidoRepository = eventoPedidoRepository;
        this.estoqueService = estoqueService;
        this.gatewayPagamento = gatewayPagamento;
    }

    @Transactional
    public TentativaPagamento processarPagamento(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pedido não encontrado")
                );

        if (pedido.getStatus() != StatusPedido.PENDENTE_PAGAMENTO) {
            throw new IllegalStateException(
                    "Pedido não está pendente de pagamento"
            );
        }

        boolean possuiPagamentoAprovado =
                tentativaPagamentoRepository.existsByPedidoIdAndResultado(
                        pedidoId,
                        StatusPagamento.APROVADO
                );

        if (possuiPagamentoAprovado) {
            throw new IllegalStateException(
                    "Pedido já possui pagamento aprovado"
            );
        }

        verificarDisponibilidade(pedido);

        StatusPagamento resultado = gatewayPagamento.processar();

        TentativaPagamento tentativa = new TentativaPagamento();
        tentativa.setPedido(pedido);
        tentativa.setResultado(resultado);

        TentativaPagamento tentativaSalva =
                tentativaPagamentoRepository.save(tentativa);

        if (resultado == StatusPagamento.APROVADO) {
            concluirPagamentoAprovado(pedido);
        } else {
            registrarEvento(
                    pedido,
                    "PAGAMENTO_RECUSADO",
                    "Pagamento recusado"
            );
        }

        return tentativaSalva;
    }

    private void verificarDisponibilidade(Pedido pedido) {

        for (ItemPedido item : pedido.getItens()) {

            boolean disponivel =
                    estoqueService.possuiDisponibilidade(
                            pedido.getUnidade().getId(),
                            item.getProduto().getId(),
                            item.getQuantidade()
                    );

            if (!disponivel) {
                throw new IllegalStateException(
                        "Estoque insuficiente para processar pagamento"
                );
            }
        }
    }

    private void concluirPagamentoAprovado(Pedido pedido) {

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.baixarEstoque(
                    pedido.getUnidade().getId(),
                    item.getProduto().getId(),
                    item.getQuantidade()
            );
        }

        pedido.setStatus(StatusPedido.PAGAMENTO_APROVADO);
        pedidoRepository.save(pedido);

        registrarEvento(
                pedido,
                "PAGAMENTO_APROVADO",
                "Pagamento aprovado"
        );
    }

    private void registrarEvento(
            Pedido pedido,
            String tipo,
            String descricao
    ) {

        EventoPedido evento = new EventoPedido();
        evento.setPedido(pedido);
        evento.setTipo(tipo);
        evento.setDescricao(descricao);

        eventoPedidoRepository.save(evento);
    }
}