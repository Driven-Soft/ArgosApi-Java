package br.com.fiap.java.ArgosApi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequestDTO(
        @NotBlank String mensagem
) {}
