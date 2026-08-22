package br.com.raizesfood.gateway;

import br.com.raizesfood.model.enums.StatusPagamento;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoMock implements GatewayPagamento {

    @Override
    public StatusPagamento processar() {
        return StatusPagamento.APROVADO;
    }
}