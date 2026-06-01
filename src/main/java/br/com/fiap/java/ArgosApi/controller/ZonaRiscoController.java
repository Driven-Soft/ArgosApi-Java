package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.ZonaRiscoRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.ZonaRiscoResponseDTO;
import br.com.fiap.java.ArgosApi.entity.NivelRisco;
import br.com.fiap.java.ArgosApi.service.ZonaRiscoService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/zonas")
public class ZonaRiscoController {

    private final ZonaRiscoService zonaRiscoService;

    public ZonaRiscoController(ZonaRiscoService zonaRiscoService) {
        this.zonaRiscoService = zonaRiscoService;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<ZonaRiscoResponseDTO>>> listar() {
        var list = zonaRiscoService.listarTodas()
                .stream()
                .map(dto -> EntityModel.of(dto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).buscar(dto.id())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).listar()).withRel("zonas")))
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ZonaRiscoResponseDTO>> buscar(@PathVariable UUID id) {
        var z = zonaRiscoService.buscarPorId(id);
        return z == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(
                EntityModel.of(z,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).buscar(id)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).listar()).withRel("zonas")));
    }

    @PostMapping
    public ResponseEntity<ZonaRiscoResponseDTO> criar(@jakarta.validation.Valid @RequestBody ZonaRiscoRequestDTO dto) {
        var created = zonaRiscoService.criar(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZonaRiscoResponseDTO> atualizar(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody ZonaRiscoRequestDTO dto) {
        var updated = zonaRiscoService.atualizar(id, dto);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/nivel-risco")
    public ResponseEntity<ZonaRiscoResponseDTO> atualizarNivel(@PathVariable UUID id, @RequestParam NivelRisco nivel) {
        var updated = zonaRiscoService.atualizarNivelRisco(id, nivel);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/analisar-risco")
    public ResponseEntity<Object> analisarRisco(@PathVariable UUID id) {
        var result = zonaRiscoService.analisarRisco(id);
        return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }
}
