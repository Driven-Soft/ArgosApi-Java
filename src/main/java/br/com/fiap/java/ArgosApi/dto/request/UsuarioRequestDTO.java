package br.com.fiap.java.ArgosApi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        @NotBlank String nome,
        String telefone
) {}
