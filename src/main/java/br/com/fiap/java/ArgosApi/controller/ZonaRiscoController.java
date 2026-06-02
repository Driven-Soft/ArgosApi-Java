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
import br.com.fiap.java.ArgosApi.service.NasaService;
import br.com.fiap.java.ArgosApi.service.NasaService.NasaImageResult;
import br.com.fiap.java.ArgosApi.service.RainMonitoringService;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/zonas")
public class ZonaRiscoController {

    private final ZonaRiscoService zonaRiscoService;
    private final NasaService nasaService;
    private final RainMonitoringService rainMonitoringService;

    public ZonaRiscoController(ZonaRiscoService zonaRiscoService, NasaService nasaService, RainMonitoringService rainMonitoringService) {
        this.zonaRiscoService = zonaRiscoService;
        this.nasaService = nasaService;
        this.rainMonitoringService = rainMonitoringService;
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

    @GetMapping("/dashboard/{id}")
    public ResponseEntity<Object> dashboard(@PathVariable UUID id) {
        var zDto = zonaRiscoService.buscarPorId(id);
        if (zDto == null) return ResponseEntity.notFound().build();

        var z = zonaRiscoService.findEntityById(id);
        if (z == null) return ResponseEntity.notFound().build();

        // chuva
        double chuva = rainMonitoringService.getRainMm(z.getLatitude(), z.getLongitude());

        // apod
        var apod = nasaService.getApod();

        var imageLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).getEarthImage(id, 0.15)).withRel("nasaEarthImage");

        return ResponseEntity.ok(java.util.Map.of(
                "zona", z,
                "chuva24h", chuva,
                "apod", apod,
                "nasaEarthImage", imageLink.getHref()
        ));
    }

    @GetMapping(value = "/{id}/nasa-earth-image")
    public ResponseEntity<byte[]> getEarthImage(@PathVariable UUID id, @RequestParam(defaultValue = "0.15") double dim) {
        var zDto = zonaRiscoService.buscarPorId(id);
        if (zDto == null) return ResponseEntity.notFound().build();

        var z = zonaRiscoService.findEntityById(id);
        if (z == null) return ResponseEntity.notFound().build();

        NasaImageResult img = nasaService.getEarthImageBytes(z.getLatitude(), z.getLongitude(), dim);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(img.contentType));
        return new ResponseEntity<>(img.bytes, headers, HttpStatus.OK);
    }
}
