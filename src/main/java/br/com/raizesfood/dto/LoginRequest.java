package br.com.raizesfood.dto;

public record LoginRequest(
        String email,
        String senha
) {
}