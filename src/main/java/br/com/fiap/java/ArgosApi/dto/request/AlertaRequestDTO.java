package br.com.fiap.java.ArgosApi.dto.request;

import br.com.fiap.java.ArgosApi.entity.NivelAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertaRequestDTO(
        @NotBlank String titulo,
        String descricao,
        @NotNull NivelAlerta nivelAlerta,
        @NotNull UUID zonaRiscoId,
        UUID usuarioCriadorId,
        LocalDateTime inicioVigencia,
        LocalDateTime fimVigencia
) {}
