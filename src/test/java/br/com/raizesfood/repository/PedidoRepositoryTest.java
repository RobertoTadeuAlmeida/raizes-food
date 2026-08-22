package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.ItemPedido;
import br.com.raizesfood.model.entity.Pedido;
import br.com.raizesfood.model.entity.Produto;
import br.com.raizesfood.model.entity.Unidade;
import br.com.raizesfood.model.enums.CanalPedido;
import br.com.raizesfood.model.enums.StatusPedido;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveSalvarPedidoComItens() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Centro");
        unidade.setEndereco("Rua Principal, 100");
        unidade = unidadeRepository.save(unidade);

        Produto produto = new Produto();
        produto.setNome("Baião de Dois");
        produto.setDescricao("Prato típico");
        produto.setPreco(new BigDecimal("24.90"));
        produto = produtoRepository.save(produto);

        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setCanal(CanalPedido.APP);

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("24.90"));
        item.setPedido(pedido);

        pedido.getItens().add(item);

        Pedido salvo = pedidoRepository.save(pedido);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getItens()).hasSize(1);
        assertThat(salvo.getStatus()).isEqualTo(StatusPedido.PENDENTE_PAGAMENTO);
        assertThat(salvo.getItens().getFirst().getPrecoUnitario())
                .isEqualByComparingTo("24.90");
    }
}