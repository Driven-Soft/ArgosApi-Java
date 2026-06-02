package br.com.fiap.java.ArgosApi.dto.request;

import br.com.fiap.java.ArgosApi.entity.NivelAlerta;
import jakarta.validation.constraints.NotNull;

public record AlertaZonaRequestDTO(
        @NotNull NivelAlerta nivelAlerta,
        String mensagemPadrao,
        Integer limiarChuvaMm
) {}
