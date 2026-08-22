package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.Estoque;
import br.com.raizesfood.model.entity.Produto;
import br.com.raizesfood.model.entity.Unidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EstoqueRepositoryTest {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveSalvarEstoqueDeProdutoPorUnidade() {

        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Centro");
        unidade.setEndereco("Rua Principal, 100");
        unidade = unidadeRepository.save(unidade);

        Produto produto = new Produto();
        produto.setNome("Baião de Dois");
        produto.setPreco(new BigDecimal("24.90"));
        produto = produtoRepository.save(produto);

        Estoque estoque = new Estoque();
        estoque.setUnidade(unidade);
        estoque.setProduto(produto);
        estoque.setQuantidade(20);

        Estoque salvo = estoqueRepository.save(estoque);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getQuantidade()).isEqualTo(20);
        assertThat(salvo.getUnidade().getId()).isEqualTo(unidade.getId());
        assertThat(salvo.getProduto().getId()).isEqualTo(produto.getId());
    }
}