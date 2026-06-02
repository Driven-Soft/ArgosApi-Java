package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.StatusOcorrencia;

import java.time.LocalDateTime;
import java.util.UUID;

public record OcorrenciaResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        StatusOcorrencia status,
        UUID tipoOcorrenciaId,
        String tipoOcorrenciaNome,
        UUID zonaRiscoId,
        String zonaRiscoNome,
        UUID usuarioId,
        Double latitude,
        Double longitude,
        LocalDateTime dataCriacao,
        LocalDateTime resolvidoEm
) {}
