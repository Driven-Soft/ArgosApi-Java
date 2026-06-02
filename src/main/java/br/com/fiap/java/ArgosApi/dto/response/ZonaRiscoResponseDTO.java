package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.NivelRisco;

import java.time.LocalDateTime;
import java.util.UUID;

public record ZonaRiscoResponseDTO(
        UUID id,
        String nome,
        String cidade,
        String estado,
        Double latitude,
        Double longitude,
        String descricao,
        NivelRisco nivelRiscoAtual,
        LocalDateTime ultimaAnalise,
        LocalDateTime dataCriacao
) {}
