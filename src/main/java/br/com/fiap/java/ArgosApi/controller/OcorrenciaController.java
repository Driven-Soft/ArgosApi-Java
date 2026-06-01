package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.OcorrenciaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.OcorrenciaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.StatusOcorrencia;
import br.com.fiap.java.ArgosApi.service.OcorrenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listar() {
        return ResponseEntity.ok(ocorrenciaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> buscar(@PathVariable UUID id) {
        var o = ocorrenciaService.buscarPorId(id);
        return o == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(o);
    }

    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> criar(@jakarta.validation.Valid @RequestBody OcorrenciaRequestDTO dto) {
        var created = ocorrenciaService.criar(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> atualizar(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody OcorrenciaRequestDTO dto) {
        var updated = ocorrenciaService.atualizar(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OcorrenciaResponseDTO> alterarStatus(@PathVariable UUID id, @RequestParam StatusOcorrencia status) {
        var updated = ocorrenciaService.alterarStatus(id, status);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }
}
