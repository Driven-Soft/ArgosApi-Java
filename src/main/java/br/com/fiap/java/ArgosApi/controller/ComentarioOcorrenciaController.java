package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.ComentarioRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.ComentarioResponseDTO;
import br.com.fiap.java.ArgosApi.service.ComentarioOcorrenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ocorrencias/{ocorrenciaId}/comentarios")
public class ComentarioOcorrenciaController {

    private final ComentarioOcorrenciaService comentarioService;

    public ComentarioOcorrenciaController(ComentarioOcorrenciaService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listar(@PathVariable UUID ocorrenciaId) {
        return ResponseEntity.ok(comentarioService.listarPorOcorrencia(ocorrenciaId));
    }

    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> criar(@PathVariable UUID ocorrenciaId, @jakarta.validation.Valid @RequestBody ComentarioRequestDTO dto) {
        var created = comentarioService.criar(ocorrenciaId, dto);
        return ResponseEntity.status(201).body(created);
    }
}
