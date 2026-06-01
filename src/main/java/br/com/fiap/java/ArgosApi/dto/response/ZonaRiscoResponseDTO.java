package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.NivelRisco;
import java.util.UUID;

public record ZonaRiscoResponseDTO(
        UUID id,
        String nome,
        String cidade,
        String estado,
        NivelRisco nivelRiscoAtual
) {
}
