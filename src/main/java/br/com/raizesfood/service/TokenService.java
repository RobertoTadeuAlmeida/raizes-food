package br.com.raizesfood.service;

import br.com.raizesfood.model.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public String gerarToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        tokens.put(token, usuario.getId());

        return token;
    }

    public Optional<Long> buscarUsuarioId(String token) {
        return Optional.ofNullable(tokens.get(token));
    }
}