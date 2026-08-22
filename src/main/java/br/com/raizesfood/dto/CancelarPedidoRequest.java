package br.com.raizesfood.dto;

import br.com.raizesfood.model.enums.PerfilUsuario;

public record CancelarPedidoRequest(
        PerfilUsuario perfil
) {
}