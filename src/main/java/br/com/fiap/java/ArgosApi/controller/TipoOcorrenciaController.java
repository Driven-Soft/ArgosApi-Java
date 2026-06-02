package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.response.TipoOcorrenciaResponseDTO;
import br.com.fiap.java.ArgosApi.service.TipoOcorrenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-ocorrencia")
@Tag(name = "Tipos de Ocorrencia", description = "Catalogo de tipos de ocorrencia disponiveis")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TipoOcorrenciaController {

    private final TipoOcorrenciaService tipoOcorrenciaService;

    @GetMapping
    @Operation(summary = "Listar todos os tipos de ocorrencia ativos")
    public ResponseEntity<List<TipoOcorrenciaResponseDTO>> listar() {
        return ResponseEntity.ok(tipoOcorrenciaService.listarTodos());
    }
}
