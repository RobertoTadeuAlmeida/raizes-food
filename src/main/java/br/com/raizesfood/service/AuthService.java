package br.com.raizesfood.service;

import br.com.raizesfood.dto.CadastroUsuarioRequest;
import br.com.raizesfood.dto.LoginRequest;
import br.com.raizesfood.dto.LoginResponse;
import br.com.raizesfood.model.entity.Usuario;
import br.com.raizesfood.model.enums.PerfilUsuario;
import br.com.raizesfood.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public Usuario cadastrarCliente(CadastroUsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());

        usuario.setSenha(
                passwordEncoder.encode(request.senha())
        );

        usuario.setPerfil(PerfilUsuario.CLIENTE);
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("Credenciais inválidas")
                );

        if (!usuario.isAtivo()) {
            throw new IllegalStateException("Usuário inativo");
        }

        boolean senhaValida = passwordEncoder.matches(
                request.senha(),
                usuario.getSenha()
        );

        if (!senhaValida) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token = tokenService.gerarToken(usuario);

        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getPerfil()
        );
    }
}