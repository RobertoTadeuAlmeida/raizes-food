package br.com.raizesfood.service;

import br.com.raizesfood.model.entity.Estoque;
import br.com.raizesfood.repository.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    private EstoqueService estoqueService;

    private Estoque estoque;

    @BeforeEach
    void setUp() {
        estoqueService = new EstoqueService(estoqueRepository);

        estoque = new Estoque();
        estoque.setQuantidade(10);
    }

    @Test
    void deveInformarQuandoExisteDisponibilidade() {
        when(estoqueRepository.findByUnidadeIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(estoque));

        boolean disponivel =
                estoqueService.possuiDisponibilidade(1L, 1L, 5);

        assertThat(disponivel).isTrue();
    }

    @Test
    void deveBaixarQuantidadeDoEstoque() {
        when(estoqueRepository.findByUnidadeIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(estoque));

        estoqueService.baixarEstoque(1L, 1L, 4);

        assertThat(estoque.getQuantidade()).isEqualTo(6);
    }

    @Test
    void naoDeveBaixarQuandoEstoqueForInsuficiente() {
        when(estoqueRepository.findByUnidadeIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(estoque));

        assertThatThrownBy(
                () -> estoqueService.baixarEstoque(1L, 1L, 15)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Estoque insuficiente");

        assertThat(estoque.getQuantidade()).isEqualTo(10);
    }

    @Test
    void deveAdicionarEntradaAoEstoque() {
        when(estoqueRepository.findByUnidadeIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(estoque));

        estoqueService.adicionarEntrada(1L, 1L, 5);

        assertThat(estoque.getQuantidade()).isEqualTo(15);
    }

    @Test
    void deveRetornarQuantidadeAoEstoque() {
        when(estoqueRepository.findByUnidadeIdAndProdutoId(1L, 1L))
                .thenReturn(Optional.of(estoque));

        estoqueService.retornarEstoque(1L, 1L, 3);

        assertThat(estoque.getQuantidade()).isEqualTo(13);
    }
}