package br.com.raizesfood.dto;

public record CadastroUsuarioRequest(
        String nome,
        String email,
        String senha,
        String telefone
) {
}