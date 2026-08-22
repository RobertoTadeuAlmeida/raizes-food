package br.com.raizesfood.controller;

import br.com.raizesfood.dto.*;
import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(
            @RequestBody CriarPedidoRequest request
    ) {
        Pedido pedido = pedidoService.criarPedido(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPedido(
            @PathVariable Long id
    ) {
        Pedido pedido = pedidoService.buscarPorId(id);

        return ResponseEntity.ok(toResponse(pedido));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusPedidoRequest request
    ) {
        Pedido pedido = pedidoService.atualizarStatus(
                id,
                request.status()
        );

        return ResponseEntity.ok(toResponse(pedido));
    }

    @PostMapping("/{id}/cancelamento")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable Long id,
            @RequestBody CancelarPedidoRequest request
    ) {
        Pedido pedido = pedidoService.cancelarPedido(
                id,
                request.perfil()
        );

        return ResponseEntity.ok(toResponse(pedido));
    }

    private PedidoResponse toResponse(Pedido pedido) {

        List<ItemPedidoResponse> itens = pedido.getItens()
                .stream()
                .map(item -> new ItemPedidoResponse(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .toList();

        Long clienteId = pedido.getCliente() != null
                ? pedido.getCliente().getId()
                : null;

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUnidade().getId(),
                clienteId,
                pedido.getCanal(),
                pedido.getStatus(),
                pedido.getCriadoEm(),
                itens
        );
    }
}