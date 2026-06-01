package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.StatusOcorrencia;
import java.time.LocalDateTime;
import java.util.UUID;

public record OcorrenciaResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        StatusOcorrencia status,
        LocalDateTime dataCriacao
) {
}
