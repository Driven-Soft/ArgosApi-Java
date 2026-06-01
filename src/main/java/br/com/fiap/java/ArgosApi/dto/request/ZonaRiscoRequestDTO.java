package br.com.fiap.java.ArgosApi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ZonaRiscoRequestDTO(
        @NotBlank String nome,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotNull Double latitude,
        @NotNull Double longitude,
        String descricao
) {
}
