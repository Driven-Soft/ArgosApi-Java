package br.com.fiap.java.ArgosApi.dto.response;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email
) {
}
