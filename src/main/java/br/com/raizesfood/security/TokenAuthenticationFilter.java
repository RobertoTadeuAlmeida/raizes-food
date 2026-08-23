package br.com.raizesfood.security;

import br.com.raizesfood.model.entity.Usuario;
import br.com.raizesfood.repository.UsuarioRepository;
import br.com.raizesfood.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public TokenAuthenticationFilter(
            TokenService tokenService,
            UsuarioRepository usuarioRepository
    ) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {

            String token = authorization.substring(7);

            tokenService.buscarUsuarioId(token)
                    .flatMap(usuarioRepository::findById)
                    .filter(Usuario::isAtivo)
                    .ifPresent(this::autenticar);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(Usuario usuario) {

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getPerfil().name()
                );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        List.of(authority)
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }
}