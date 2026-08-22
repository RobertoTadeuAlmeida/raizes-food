package br.com.raizesfood.service;

import br.com.raizesfood.dto.CriarPedidoRequest;
import br.com.raizesfood.dto.ItemPedidoRequest;
import br.com.raizesfood.model.entity.ItemPedido;
import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.model.entity.Produto;
import br.com.raizesfood.model.entity.Unidade;
import br.com.raizesfood.model.enums.CanalPedido;
import br.com.raizesfood.model.enums.PerfilUsuario;
import br.com.raizesfood.model.enums.StatusPedido;
import br.com.raizesfood.repository.EventoPedidoRepository;
import br.com.raizesfood.repository.PedidoRepository;
import br.com.raizesfood.repository.ProdutoRepository;
import br.com.raizesfood.repository.UnidadeRepository;
import br.com.raizesfood.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UnidadeRepository unidadeRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EventoPedidoRepository eventoPedidoRepository;

    @Mock
    private EstoqueService estoqueService;

    private PedidoService pedidoService;

    private Unidade unidade;
    private Produto produto;

    @BeforeEach
    void setUp() {

        pedidoService = new PedidoService(
                pedidoRepository,
                unidadeRepository,
                produtoRepository,
                usuarioRepository,
                eventoPedidoRepository,
                estoqueService
        );

        unidade = new Unidade();
        unidade.setNome("Unidade Centro");
        unidade.setEndereco("Rua Principal, 100");
        unidade.setAtiva(true);

        produto = new Produto();
        produto.setNome("Baião de Dois");
        produto.setPreco(new BigDecimal("24.90"));
        produto.setAtivo(true);
    }

    @Test
    void deveCriarPedidoComItem() {

        CriarPedidoRequest request = new CriarPedidoRequest(
                1L,
                null,
                CanalPedido.TOTEM,
                List.of(
                        new ItemPedidoRequest(1L, 2)
                )
        );

        when(unidadeRepository.findById(1L))
                .thenReturn(Optional.of(unidade));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(estoqueService.possuiDisponibilidade(
                any(),
                any(),
                eq(2)
        )).thenReturn(true);

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedido = pedidoService.criarPedido(request);

        assertThat(pedido.getItens()).hasSize(1);

        assertThat(pedido.getStatus())
                .isEqualTo(StatusPedido.PENDENTE_PAGAMENTO);

        assertThat(pedido.getItens().getFirst().getPrecoUnitario())
                .isEqualByComparingTo("24.90");

        assertThat(pedido.getCliente()).isNull();

        verify(eventoPedidoRepository)
                .save(any());
    }

    @Test
    void naoDeveCriarPedidoSemEstoqueSuficiente() {

        CriarPedidoRequest request = new CriarPedidoRequest(
                1L,
                null,
                CanalPedido.TOTEM,
                List.of(
                        new ItemPedidoRequest(1L, 20)
                )
        );

        when(unidadeRepository.findById(1L))
                .thenReturn(Optional.of(unidade));

        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));

        when(estoqueService.possuiDisponibilidade(
                any(),
                any(),
                eq(20)
        )).thenReturn(false);

        assertThatThrownBy(
                () -> pedidoService.criarPedido(request)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Produto sem estoque suficiente");
    }

    @Test
    void deveAvancarPedidoDePagamentoAprovadoParaEmPreparacao() {

        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.PAGAMENTO_APROVADO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido atualizado = pedidoService.atualizarStatus(
                1L,
                StatusPedido.EM_PREPARACAO
        );

        assertThat(atualizado.getStatus())
                .isEqualTo(StatusPedido.EM_PREPARACAO);

        verify(eventoPedidoRepository).save(any());
    }

    @Test
    void naoDevePermitirTransicaoDeStatusInvalida() {

        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.PENDENTE_PAGAMENTO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        assertThatThrownBy(() ->
                pedidoService.atualizarStatus(
                        1L,
                        StatusPedido.FINALIZADO
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Transição de status inválida");

        verify(pedidoRepository, never()).save(any());
        verify(eventoPedidoRepository, never()).save(any());
    }

    @Test
    void clienteDeveCancelarPedidoPendenteSemRetornarEstoque() {
        Pedido pedido = new Pedido();

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido cancelado = pedidoService.cancelarPedido(
                1L,
                PerfilUsuario.CLIENTE
        );

        assertThat(cancelado.getStatus())
                .isEqualTo(StatusPedido.CANCELADO);

        verify(estoqueService, never())
                .retornarEstoque(any(), any(), anyInt());

        verify(eventoPedidoRepository).save(any());
    }

    @Test
    void clienteNaoDeveCancelarPedidoEmPreparacao() {
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.EM_PREPARACAO);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        assertThatThrownBy(() ->
                pedidoService.cancelarPedido(
                        1L,
                        PerfilUsuario.CLIENTE
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pedido não pode ser cancelado neste status");

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void gerenteDeveCancelarPedidoEmPreparacaoERetornarEstoque() {
        Unidade unidade = new Unidade();
        Produto produto = new Produto();

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setStatus(StatusPedido.EM_PREPARACAO);
        pedido.getItens().add(item);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pedido cancelado = pedidoService.cancelarPedido(
                1L,
                PerfilUsuario.GERENTE
        );

        assertThat(cancelado.getStatus())
                .isEqualTo(StatusPedido.CANCELADO);

        verify(estoqueService)
                .retornarEstoque(any(), any(), eq(2));

        verify(eventoPedidoRepository).save(any());
    }
}