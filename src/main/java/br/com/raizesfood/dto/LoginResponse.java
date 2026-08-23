package br.com.raizesfood.dto;

import br.com.raizesfood.model.enums.PerfilUsuario;

public record LoginResponse(
        String token,
        Long usuarioId,
        String nome,
        PerfilUsuario perfil
) {
}