package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.ComentarioRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.ComentarioResponseDTO;
import br.com.fiap.java.ArgosApi.service.ComentarioOcorrenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ocorrencias/{ocorrenciaId}/comentarios")
@Tag(name = "Comentarios", description = "Comentarios em ocorrencias")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ComentarioOcorrenciaController {

    private final ComentarioOcorrenciaService comentarioService;

    @GetMapping
    @Operation(summary = "Listar comentarios de uma ocorrencia")
    public ResponseEntity<List<ComentarioResponseDTO>> listar(@PathVariable UUID ocorrenciaId) {
        return ResponseEntity.ok(comentarioService.listarPorOcorrencia(ocorrenciaId));
    }

    @PostMapping
    @Operation(summary = "Adicionar comentario a uma ocorrencia")
    public ResponseEntity<ComentarioResponseDTO> criar(@PathVariable UUID ocorrenciaId,
                                                        @RequestBody @Valid ComentarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.criar(ocorrenciaId, dto));
    }
}
