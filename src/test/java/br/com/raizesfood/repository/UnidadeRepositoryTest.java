package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.Unidade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UnidadeRepositoryTest {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Test
    void deveSalvarUnidade() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Centro");
        unidade.setEndereco("Rua Principal, 100");
        unidade.setTelefone("31999999999");
        unidade.setEmail("centro@raizesfood.com");

        Unidade salva = unidadeRepository.save(unidade);

        assertThat(salva.getId()).isNotNull();
        assertThat(salva.getNome()).isEqualTo("Unidade Centro");
        assertThat(salva.isAtiva()).isTrue();
    }
}