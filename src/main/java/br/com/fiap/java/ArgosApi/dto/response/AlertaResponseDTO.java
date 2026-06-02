package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.NivelAlerta;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertaResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        NivelAlerta nivelAlerta,
        UUID zonaRiscoId,
        String zonaRiscoNome,
        LocalDateTime inicioVigencia,
        LocalDateTime fimVigencia,
        LocalDateTime dataCriacao,
        boolean ativo
) {}
