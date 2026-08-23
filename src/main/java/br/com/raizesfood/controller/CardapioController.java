package br.com.raizesfood.controller;

import br.com.raizesfood.dto.ProdutoCardapioResponse;
import br.com.raizesfood.service.CardapioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class CardapioController {

    private final CardapioService cardapioService;

    public CardapioController(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    @GetMapping("/{unidadeId}/cardapio")
    public ResponseEntity<List<ProdutoCardapioResponse>> buscarCardapio(
            @PathVariable Long unidadeId
    ) {

        return ResponseEntity.ok(
                cardapioService.buscarPorUnidade(unidadeId)
        );
    }
}