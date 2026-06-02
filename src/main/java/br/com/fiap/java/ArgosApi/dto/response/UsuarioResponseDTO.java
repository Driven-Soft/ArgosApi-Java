package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.TipoUsuario;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String telefone,
        TipoUsuario tipoUsuario
) {}
