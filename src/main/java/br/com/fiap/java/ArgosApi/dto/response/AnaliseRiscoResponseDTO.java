package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.NivelRisco;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnaliseRiscoResponseDTO(
        UUID id,
        UUID zonaRiscoId,
        Double chuvaMm24h,
        Integer scoreFinal,
        NivelRisco nivelRisco,
        String fonteDados,
        LocalDateTime dataAnalise
) {}
