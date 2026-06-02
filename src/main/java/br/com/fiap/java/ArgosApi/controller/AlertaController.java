package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.AlertaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AlertaResponseDTO;
import br.com.fiap.java.ArgosApi.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas", description = "Gerenciamento de alertas de desastres")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    @Operation(summary = "Listar todos os alertas com HATEOAS")
    public ResponseEntity<List<EntityModel<AlertaResponseDTO>>> listar() {
        var list = alertaService.listarTodos().stream()
                .map(dto -> EntityModel.of(dto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AlertaController.class).buscar(dto.id())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AlertaController.class).listar()).withRel("alertas")))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID com HATEOAS")
    public ResponseEntity<EntityModel<AlertaResponseDTO>> buscar(@PathVariable UUID id) {
        var a = alertaService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(a,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AlertaController.class).buscar(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AlertaController.class).listar()).withRel("alertas")));
    }

    @PostMapping
    @Operation(summary = "Criar novo alerta")
    public ResponseEntity<AlertaResponseDTO> criar(@RequestBody @Valid AlertaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertaService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar alerta")
    public ResponseEntity<AlertaResponseDTO> atualizar(@PathVariable UUID id,
                                                        @RequestBody @Valid AlertaRequestDTO dto) {
        return ResponseEntity.ok(alertaService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar ou desativar alerta")
    public ResponseEntity<AlertaResponseDTO> alterarStatus(@PathVariable UUID id,
                                                            @RequestParam boolean ativo) {
        return ResponseEntity.ok(alertaService.alterarStatus(id, ativo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar alerta")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        alertaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
