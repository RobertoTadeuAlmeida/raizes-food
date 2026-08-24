package br.com.raizesfood.service;

import br.com.raizesfood.dto.CriarPedidoRequest;
import br.com.raizesfood.dto.ItemPedidoRequest;
import br.com.raizesfood.model.entity.EventoPedido;
import br.com.raizesfood.model.entity.ItemPedido;
import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.model.entity.Produto;
import br.com.raizesfood.model.entity.Unidade;
import br.com.raizesfood.model.entity.Usuario;
import br.com.raizesfood.model.enums.CanalPedido;
import br.com.raizesfood.model.enums.PerfilUsuario;
import br.com.raizesfood.model.enums.StatusPedido;
import br.com.raizesfood.repository.EventoPedidoRepository;
import br.com.raizesfood.repository.PedidoRepository;
import br.com.raizesfood.repository.ProdutoRepository;
import br.com.raizesfood.repository.UnidadeRepository;
import br.com.raizesfood.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoPedidoRepository eventoPedidoRepository;
    private final EstoqueService estoqueService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            UnidadeRepository unidadeRepository,
            ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository,
            EventoPedidoRepository eventoPedidoRepository,
            EstoqueService estoqueService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoPedidoRepository = eventoPedidoRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public Pedido criarPedido(CriarPedidoRequest request) {

        validarRequest(request);

        Unidade unidade = unidadeRepository.findById(request.unidadeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Unidade não encontrada")
                );

        if (!unidade.isAtiva()) {
            throw new IllegalStateException("Unidade inativa");
        }

        Usuario cliente = buscarCliente(request.clienteId());

        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setCliente(cliente);
        pedido.setCanal(request.canalPedido());

        for (ItemPedidoRequest itemRequest : request.itens()) {

            ItemPedido item = criarItem(
                    unidade,
                    pedido,
                    itemRequest
            );

            pedido.getItens().add(item);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        registrarEvento(
                pedidoSalvo,
                "PEDIDO_CRIADO",
                "Pedido criado"
        );

        return pedidoSalvo;
    }

    private ItemPedido criarItem(
            Unidade unidade,
            Pedido pedido,
            ItemPedidoRequest request
    ) {

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade do item deve ser maior que zero"
            );
        }

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado")
                );

        if (!produto.isAtivo()) {
            throw new IllegalStateException("Produto inativo");
        }

        boolean disponivel = estoqueService.possuiDisponibilidade(
                unidade.getId(),
                produto.getId(),
                request.quantidade()
        );

        if (!disponivel) {
            throw new IllegalStateException("Produto sem estoque suficiente");
        }

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(request.quantidade());

        // Preserva o preço praticado no momento da compra.
        item.setPrecoUnitario(produto.getPreco());

        return item;
    }

    private Usuario buscarCliente(Long clienteId) {

        if (clienteId == null) {
            return null;
        }

        return usuarioRepository.findById(clienteId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cliente não encontrado")
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

    private void validarRequest(CriarPedidoRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Pedido não informado");
        }

        if (request.unidadeId() == null) {
            throw new IllegalArgumentException("Unidade obrigatória");
        }

        if (request.canalPedido() == null) {
            throw new IllegalArgumentException("Canal obrigatório");
        }

        if (request.itens() == null || request.itens().isEmpty()) {
            throw new IllegalArgumentException(
                    "Pedido deve possuir pelo menos um item"
            );
        }
    }

    @Transactional
    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pedido não encontrado")
                );

        if (!transicaoValida(pedido.getStatus(), novoStatus)) {
            throw new IllegalStateException(
                    "Transição de status inválida"
            );
        }

        StatusPedido statusAnterior = pedido.getStatus();

        pedido.setStatus(novoStatus);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        registrarEvento(
                pedidoSalvo,
                "STATUS_ALTERADO",
                "Status alterado de " + statusAnterior + " para " + novoStatus
        );

        return pedidoSalvo;
    }

    private boolean transicaoValida(
            StatusPedido atual,
            StatusPedido novoStatus
    ) {
        return switch (atual) {
            case PAGAMENTO_APROVADO -> novoStatus == StatusPedido.EM_PREPARACAO;

            case EM_PREPARACAO -> novoStatus == StatusPedido.PRONTO;

            case PRONTO -> novoStatus == StatusPedido.FINALIZADO;

            default -> false;
        };
    }

    @Transactional
    public Pedido cancelarPedido(
            Long pedidoId,
            PerfilUsuario perfil
    ) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pedido não encontrado")
                );

        validarCancelamento(pedido, perfil);

        StatusPedido statusAnterior = pedido.getStatus();

        if (houveBaixaEstoque(statusAnterior)) {
            retornarEstoque(pedido);
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        registrarEvento(
                pedidoSalvo,
                "PEDIDO_CANCELADO",
                "Pedido cancelado"
        );

        return pedidoSalvo;
    }

    private void validarCancelamento(
            Pedido pedido,
            PerfilUsuario perfil
    ) {
        if (perfil == null) {
            throw new IllegalArgumentException(
                    "Perfil obrigatório para cancelamento"
            );
        }

        StatusPedido status = pedido.getStatus();

        boolean permitido;

        switch (perfil) {
            case CLIENTE -> permitido =
                    status == StatusPedido.PENDENTE_PAGAMENTO
                            || status == StatusPedido.PAGAMENTO_APROVADO;

            case GERENTE, ADMINISTRADOR -> permitido =
                    status == StatusPedido.PENDENTE_PAGAMENTO
                            || status == StatusPedido.PAGAMENTO_APROVADO
                            || status == StatusPedido.EM_PREPARACAO;

            default -> permitido = false;
        }

        if (!permitido) {
            throw new IllegalStateException(
                    "Pedido não pode ser cancelado neste status"
            );
        }
    }

    private boolean houveBaixaEstoque(StatusPedido status) {
        return status == StatusPedido.PAGAMENTO_APROVADO
                || status == StatusPedido.EM_PREPARACAO;
    }

    private void retornarEstoque(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            estoqueService.retornarEstoque(
                    pedido.getUnidade().getId(),
                    item.getProduto().getId(),
                    item.getQuantidade()
            );
        }
    }

    public Pedido buscarPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Pedido não encontrado")
                );
    }
    public List<Pedido> buscarPorCanal(CanalPedido canalPedido) {
        return pedidoRepository.findByCanal(canalPedido);
    }
}