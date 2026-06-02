package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.OcorrenciaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.OcorrenciaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.StatusOcorrencia;
import br.com.fiap.java.ArgosApi.service.OcorrenciaService;
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
@RequestMapping("/api/ocorrencias")
@Tag(name = "Ocorrencias", description = "Registro e gerenciamento de ocorrencias de desastres")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    @GetMapping
    @Operation(summary = "Listar todas as ocorrencias com HATEOAS")
    public ResponseEntity<List<EntityModel<OcorrenciaResponseDTO>>> listar() {
        var list = ocorrenciaService.listarTodas().stream()
                .map(dto -> EntityModel.of(dto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OcorrenciaController.class).buscar(dto.id())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OcorrenciaController.class).listar()).withRel("ocorrencias")))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ocorrencia por ID com HATEOAS")
    public ResponseEntity<EntityModel<OcorrenciaResponseDTO>> buscar(@PathVariable UUID id) {
        var o = ocorrenciaService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(o,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OcorrenciaController.class).buscar(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OcorrenciaController.class).listar()).withRel("ocorrencias")));
    }

    @PostMapping
    @Operation(summary = "Registrar nova ocorrencia (usuario autenticado e associado automaticamente)")
    public ResponseEntity<OcorrenciaResponseDTO> criar(@RequestBody @Valid OcorrenciaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ocorrenciaService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ocorrencia")
    public ResponseEntity<OcorrenciaResponseDTO> atualizar(@PathVariable UUID id,
                                                            @RequestBody @Valid OcorrenciaRequestDTO dto) {
        return ResponseEntity.ok(ocorrenciaService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status da ocorrencia")
    public ResponseEntity<OcorrenciaResponseDTO> alterarStatus(@PathVariable UUID id,
                                                                @RequestParam StatusOcorrencia status) {
        return ResponseEntity.ok(ocorrenciaService.alterarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar ocorrencia")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        ocorrenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
