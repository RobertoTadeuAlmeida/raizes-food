package br.com.raizesfood.controller;

import br.com.raizesfood.model.entity.TentativaPagamento;
import br.com.raizesfood.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/{pedidoId}/pagamento")
    public ResponseEntity<Void> processarPagamento(
            @PathVariable Long pedidoId
    ) {
        pagamentoService.processarPagamento(pedidoId);

        return ResponseEntity.ok().build();
    }
}