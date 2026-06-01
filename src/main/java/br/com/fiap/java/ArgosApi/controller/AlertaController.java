package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.AlertaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AlertaResponseDTO;
import br.com.fiap.java.ArgosApi.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public ResponseEntity<List<AlertaResponseDTO>> listar() {
        return ResponseEntity.ok(alertaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> buscar(@PathVariable UUID id) {
        var a = alertaService.buscarPorId(id);
        return a == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(a);
    }

    @PostMapping
    public ResponseEntity<AlertaResponseDTO> criar(@jakarta.validation.Valid @RequestBody AlertaRequestDTO dto) {
        var created = alertaService.criar(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> atualizar(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody AlertaRequestDTO dto) {
        var updated = alertaService.atualizar(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AlertaResponseDTO> alterarStatus(@PathVariable UUID id, @RequestParam boolean ativo) {
        var updated = alertaService.alterarStatus(id, ativo);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }
}
