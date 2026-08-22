package br.com.raizesfood.repository;

import br.com.raizesfood.model.entity.Usuario;
import br.com.raizesfood.model.enums.PerfilUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha-segura");
        usuario.setTelefone("31999999999");
        usuario.setPerfil(PerfilUsuario.CLIENTE);
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getEmail()).isEqualTo("teste@email.com");
        assertThat(salvo.getPerfil()).isEqualTo(PerfilUsuario.CLIENTE);
    }
}