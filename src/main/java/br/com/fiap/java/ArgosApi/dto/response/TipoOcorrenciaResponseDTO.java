package br.com.fiap.java.ArgosApi.dto.response;

import java.util.UUID;

public record TipoOcorrenciaResponseDTO(
        UUID id,
        String nome,
        String descricao
) {}
