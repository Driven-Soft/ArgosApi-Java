package br.com.fiap.java.ArgosApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OcorrenciaRequestDTO(
        @NotBlank String titulo,
        String descricao,
        @NotNull UUID tipoOcorrenciaId,
        @NotNull UUID zonaRiscoId,
        Double latitude,
        Double longitude
) {
}
