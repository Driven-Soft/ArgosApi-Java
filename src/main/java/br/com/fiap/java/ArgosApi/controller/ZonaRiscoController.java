package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.ZonaRiscoRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AnaliseRiscoResponseDTO;
import br.com.fiap.java.ArgosApi.dto.response.ZonaRiscoResponseDTO;
import br.com.fiap.java.ArgosApi.entity.NivelRisco;
import br.com.fiap.java.ArgosApi.service.NasaService;
import br.com.fiap.java.ArgosApi.service.RainMonitoringService;
import br.com.fiap.java.ArgosApi.service.ZonaRiscoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/zonas")
@Tag(name = "Zonas de Risco", description = "Gerenciamento de zonas de risco e analise via APIs externas")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ZonaRiscoController {

    private final ZonaRiscoService zonaRiscoService;
    private final NasaService nasaService;
    private final RainMonitoringService rainMonitoringService;

    @GetMapping
    @Operation(summary = "Listar todas as zonas de risco com HATEOAS")
    public ResponseEntity<List<EntityModel<ZonaRiscoResponseDTO>>> listar() {
        var list = zonaRiscoService.listarTodas().stream()
                .map(dto -> EntityModel.of(dto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).buscar(dto.id())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).listar()).withRel("zonas"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).analisarRisco(dto.id())).withRel("analisar-risco")))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar zona de risco por ID com HATEOAS")
    public ResponseEntity<EntityModel<ZonaRiscoResponseDTO>> buscar(@PathVariable UUID id) {
        var z = zonaRiscoService.buscarPorId(id);
        return ResponseEntity.ok(EntityModel.of(z,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).buscar(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).listar()).withRel("zonas"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).analisarRisco(id)).withRel("analisar-risco")));
    }

    @PostMapping
    @Operation(summary = "Criar nova zona de risco")
    public ResponseEntity<ZonaRiscoResponseDTO> criar(@RequestBody @Valid ZonaRiscoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zonaRiscoService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar zona de risco")
    public ResponseEntity<ZonaRiscoResponseDTO> atualizar(@PathVariable UUID id,
                                                           @RequestBody @Valid ZonaRiscoRequestDTO dto) {
        return ResponseEntity.ok(zonaRiscoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar zona de risco")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        zonaRiscoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/nivel-risco")
    @Operation(summary = "Atualizar nivel de risco manualmente")
    public ResponseEntity<ZonaRiscoResponseDTO> atualizarNivel(@PathVariable UUID id,
                                                                 @RequestParam NivelRisco nivel) {
        return ResponseEntity.ok(zonaRiscoService.atualizarNivelRisco(id, nivel));
    }

    @PostMapping("/{id}/analisar-risco")
    @Operation(summary = "Analisar risco usando dados de chuva em tempo real (Open-Meteo)")
    public ResponseEntity<Map<String, Object>> analisarRisco(@PathVariable UUID id) {
        return ResponseEntity.ok(zonaRiscoService.analisarRisco(id));
    }

    @GetMapping("/{id}/analises")
    @Operation(summary = "Historico de analises de risco da zona")
    public ResponseEntity<List<AnaliseRiscoResponseDTO>> listarAnalises(@PathVariable UUID id) {
        return ResponseEntity.ok(zonaRiscoService.listarAnalises(id));
    }

    @GetMapping("/dashboard/{id}")
    @Operation(summary = "Dashboard da zona: chuva atual + foto do dia NASA")
    public ResponseEntity<Map<String, Object>> dashboard(@PathVariable UUID id) {
        var z = zonaRiscoService.findEntityById(id);
        double chuva = rainMonitoringService.getRainMm(
                z.getLocalizacao().getLatitude(), z.getLocalizacao().getLongitude());
        var apod = nasaService.getApod();
        var imageLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ZonaRiscoController.class).getEarthImage(id, 0.15))
                .withRel("nasaEarthImage");
        return ResponseEntity.ok(Map.of(
                "zona", zonaRiscoService.buscarPorId(id),
                "chuva24h", chuva,
                "apod", apod,
                "nasaEarthImageUrl", imageLink.getHref()
        ));
    }

    @GetMapping("/{id}/nasa-earth-image")
    @Operation(summary = "Imagem de satelite da zona via NASA Earth Imagery")
    public ResponseEntity<byte[]> getEarthImage(@PathVariable UUID id,
                                                 @RequestParam(defaultValue = "0.15") double dim) {
        var z = zonaRiscoService.findEntityById(id);
        var img = nasaService.getEarthImageBytes(
                z.getLocalizacao().getLatitude(), z.getLocalizacao().getLongitude(), dim);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(img.contentType()));
        return new ResponseEntity<>(img.bytes(), headers, HttpStatus.OK);
    }
}
