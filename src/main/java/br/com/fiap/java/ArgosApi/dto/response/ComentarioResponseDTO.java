package br.com.fiap.java.ArgosApi.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComentarioResponseDTO(
        UUID id,
        String mensagem,
        UUID usuarioId,
        String usuarioNome,
        UUID ocorrenciaId,
        LocalDateTime dataCriacao
) {}
