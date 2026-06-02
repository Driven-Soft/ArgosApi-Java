package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.AlertaZonaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AlertaZonaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.AlertaZona;
import br.com.fiap.java.ArgosApi.entity.AlertaZonaId;
import br.com.fiap.java.ArgosApi.entity.NivelAlerta;
import br.com.fiap.java.ArgosApi.exception.ResourceNotFoundException;
import br.com.fiap.java.ArgosApi.repository.AlertaZonaRepository;
import br.com.fiap.java.ArgosApi.repository.ZonaRiscoRepository;
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
@RequestMapping("/api/zonas/{zonaId}/configuracoes-alerta")
@Tag(name = "Configuracoes de Alerta por Zona", description = "Gerencia configuracoes de alerta usando chave composta (zonaId + nivelAlerta)")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AlertaZonaController {

    private final AlertaZonaRepository alertaZonaRepository;
    private final ZonaRiscoRepository zonaRiscoRepository;

    @GetMapping
    @Operation(summary = "Listar configuracoes de alerta da zona (chave composta)")
    public ResponseEntity<List<AlertaZonaResponseDTO>> listar(@PathVariable UUID zonaId) {
        return ResponseEntity.ok(
                alertaZonaRepository.findByZonaRiscoId(zonaId)
                        .stream().map(this::toResponse).toList());
    }

    @PostMapping
    @Operation(summary = "Criar ou atualizar configuracao de alerta para nivel especifico (chave composta: zonaId + nivelAlerta)")
    public ResponseEntity<AlertaZonaResponseDTO> criar(@PathVariable UUID zonaId,
                                                        @RequestBody @Valid AlertaZonaRequestDTO dto) {
        var zona = zonaRiscoRepository.findById(zonaId)
                .orElseThrow(() -> new ResourceNotFoundException("Zona de risco nao encontrada"));

        var id = new AlertaZonaId(zonaId, dto.nivelAlerta());
        var config = AlertaZona.builder()
                .id(id)
                .zonaRisco(zona)
                .mensagemPadrao(dto.mensagemPadrao())
                .limiarChuvaMm(dto.limiarChuvaMm())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(alertaZonaRepository.save(config)));
    }

    @DeleteMapping("/{nivelAlerta}")
    @Operation(summary = "Remover configuracao de alerta pela chave composta (zonaId + nivelAlerta)")
    public ResponseEntity<Void> deletar(@PathVariable UUID zonaId,
                                         @PathVariable NivelAlerta nivelAlerta) {
        var id = new AlertaZonaId(zonaId, nivelAlerta);
        if (!alertaZonaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Configuracao nao encontrada");
        }
        alertaZonaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private AlertaZonaResponseDTO toResponse(AlertaZona a) {
        return new AlertaZonaResponseDTO(
                a.getId().getZonaRiscoId(),
                a.getId().getNivelAlerta(),
                a.getMensagemPadrao(),
                a.getLimiarChuvaMm(),
                a.isAtivo());
    }
}
