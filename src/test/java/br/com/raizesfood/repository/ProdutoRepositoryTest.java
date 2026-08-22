package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveSalvarProduto() {
        Produto produto = new Produto();
        produto.setNome("Baião de Dois");
        produto.setDescricao("Prato típico nordestino");
        produto.setPreco(new BigDecimal("24.90"));

        Produto salvo = produtoRepository.save(produto);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Baião de Dois");
        assertThat(salvo.getPreco()).isEqualByComparingTo("24.90");
        assertThat(salvo.isAtivo()).isTrue();
    }
}