package br.com.fiap.java.ArgosApi.dto.response;

import br.com.fiap.java.ArgosApi.entity.NivelAlerta;

import java.util.UUID;

public record AlertaZonaResponseDTO(
        UUID zonaRiscoId,
        NivelAlerta nivelAlerta,
        String mensagemPadrao,
        Integer limiarChuvaMm,
        boolean ativo
) {}
