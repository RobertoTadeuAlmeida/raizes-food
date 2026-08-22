package br.com.raizesfood.service;

import br.com.raizesfood.gateway.GatewayPagamento;
import br.com.raizesfood.model.entity.ItemPedido;
import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.model.entity.Produto;
import br.com.raizesfood.model.entity.TentativaPagamento;
import br.com.raizesfood.model.entity.Unidade;
import br.com.raizesfood.model.enums.StatusPagamento;
import br.com.raizesfood.model.enums.StatusPedido;
import br.com.raizesfood.repository.EventoPedidoRepository;
import br.com.raizesfood.repository.PedidoRepository;
import br.com.raizesfood.repository.TentativaPagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private TentativaPagamentoRepository tentativaPagamentoRepository;

    @Mock
    private EventoPedidoRepository eventoPedidoRepository;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private GatewayPagamento gatewayPagamento;

    private PagamentoService pagamentoService;
    private Pedido pedido;

    @BeforeEach
    void setUp() {

        pagamentoService = new PagamentoService(
                pedidoRepository,
                tentativaPagamentoRepository,
                eventoPedidoRepository,
                estoqueService,
                gatewayPagamento
        );

        Unidade unidade = new Unidade();

        Produto produto = new Produto();

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(2);

        pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.getItens().add(item);
    }

    @Test
    void deveAprovarPagamentoEBaixarEstoque() {

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(tentativaPagamentoRepository
                .existsByPedidoIdAndResultado(
                        1L,
                        StatusPagamento.APROVADO
                ))
                .thenReturn(false);

        when(estoqueService.possuiDisponibilidade(
                any(),
                any(),
                anyInt()
        )).thenReturn(true);

        when(gatewayPagamento.processar())
                .thenReturn(StatusPagamento.APROVADO);

        when(tentativaPagamentoRepository.save(
                any(TentativaPagamento.class)
        )).thenAnswer(invocation -> invocation.getArgument(0));

        TentativaPagamento tentativa =
                pagamentoService.processarPagamento(1L);

        assertThat(tentativa.getResultado())
                .isEqualTo(StatusPagamento.APROVADO);

        assertThat(pedido.getStatus())
                .isEqualTo(StatusPedido.PAGAMENTO_APROVADO);

        verify(estoqueService)
                .baixarEstoque(any(), any(), anyInt());

        verify(eventoPedidoRepository).save(any());
    }

    @Test
    void pagamentoRecusadoNaoDeveBaixarEstoque() {

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(tentativaPagamentoRepository
                .existsByPedidoIdAndResultado(
                        1L,
                        StatusPagamento.APROVADO
                ))
                .thenReturn(false);

        when(estoqueService.possuiDisponibilidade(
                any(),
                any(),
                anyInt()
        )).thenReturn(true);

        when(gatewayPagamento.processar())
                .thenReturn(StatusPagamento.RECUSADO);

        when(tentativaPagamentoRepository.save(
                any(TentativaPagamento.class)
        )).thenAnswer(invocation -> invocation.getArgument(0));

        TentativaPagamento tentativa =
                pagamentoService.processarPagamento(1L);

        assertThat(tentativa.getResultado())
                .isEqualTo(StatusPagamento.RECUSADO);

        assertThat(pedido.getStatus())
                .isEqualTo(StatusPedido.PENDENTE_PAGAMENTO);

        verify(estoqueService, never())
                .baixarEstoque(anyLong(), anyLong(), anyInt());

        verify(eventoPedidoRepository).save(any());
    }
}