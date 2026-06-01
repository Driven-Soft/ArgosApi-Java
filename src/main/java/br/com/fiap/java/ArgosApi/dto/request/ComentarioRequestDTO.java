package br.com.fiap.java.ArgosApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ComentarioRequestDTO(
        @NotBlank String mensagem,
        @NotNull UUID usuarioId
) {
}
