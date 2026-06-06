package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.ZonaRiscoRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AnaliseRiscoResponseDTO;
import br.com.fiap.java.ArgosApi.dto.response.ZonaRiscoResponseDTO;
import br.com.fiap.java.ArgosApi.entity.*;
import br.com.fiap.java.ArgosApi.exception.ResourceNotFoundException;
import br.com.fiap.java.ArgosApi.repository.AnaliseRiscoRepository;
import br.com.fiap.java.ArgosApi.repository.ZonaRiscoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZonaRiscoService {

    private final ZonaRiscoRepository zonaRiscoRepository;
    private final AnaliseRiscoRepository analiseRiscoRepository;
    private final RainMonitoringService rainMonitoringService;

    public List<ZonaRiscoResponseDTO> listarTodas() {
        return zonaRiscoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ZonaRiscoResponseDTO buscarPorId(UUID id) {
        return toResponse(findOrThrow(id));
    }

    public ZonaRisco findEntityById(UUID id) {
        return findOrThrow(id);
    }

    @Transactional
    public ZonaRiscoResponseDTO criar(ZonaRiscoRequestDTO dto) {
        var z = ZonaRisco.builder()
                .nome(dto.nome())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .localizacao(new Localizacao(dto.latitude(), dto.longitude()))
                .descricao(dto.descricao())
                .build();
        return toResponse(zonaRiscoRepository.save(z));
    }

    @Transactional
    public ZonaRiscoResponseDTO atualizar(UUID id, ZonaRiscoRequestDTO dto) {
        var z = findOrThrow(id);
        z.setNome(dto.nome());
        z.setCidade(dto.cidade());
        z.setEstado(dto.estado());
        z.setLocalizacao(new Localizacao(dto.latitude(), dto.longitude()));
        z.setDescricao(dto.descricao());
        return toResponse(zonaRiscoRepository.save(z));
    }

    @Transactional
    public void deletar(UUID id) {
        findOrThrow(id);
        zonaRiscoRepository.deleteById(id);
    }

    @Transactional
    public ZonaRiscoResponseDTO atualizarNivelRisco(UUID id, NivelRisco nivel) {
        var z = findOrThrow(id);
        z.setNivelRiscoAtual(nivel);
        return toResponse(zonaRiscoRepository.save(z));
    }

    @Transactional
    public Map<String, Object> analisarRisco(UUID id) {
        var z = findOrThrow(id);
        double chuva = rainMonitoringService.getRainMm(
                z.getLocalizacao().getLatitude(), z.getLocalizacao().getLongitude());

        NivelRisco nivel = calcularNivel(chuva);
        int score = (int) Math.min(100, chuva * 1.5);

        var analise = AnaliseRisco.builder()
                .zonaRisco(z)
                .chuvaMm24h(chuva)
                .scoreFinal(score)
                .nivelRisco(nivel)
                .fonteDados("OPEN-METEO")
                .build();
        analiseRiscoRepository.save(analise);

        z.setNivelRiscoAtual(nivel);
        z.setUltimaAnalise(LocalDateTime.now());
        zonaRiscoRepository.save(z);

        return Map.of(
                "zonaId", z.getId(),
                "zona", z.getNome(),
                "chuva24h", chuva,
                "score", score,
                "nivelRisco", nivel
        );
    }

    public List<AnaliseRiscoResponseDTO> listarAnalises(UUID id) {
        findOrThrow(id);
        return analiseRiscoRepository.findByZonaRiscoIdOrderByDataAnaliseDesc(id)
                .stream()
                .map(a -> new AnaliseRiscoResponseDTO(
                        a.getId(), a.getZonaRisco().getId(),
                        a.getChuvaMm24h(), a.getScoreFinal(),
                        a.getNivelRisco(), a.getFonteDados(), a.getDataAnalise()))
                .toList();
    }

    private NivelRisco calcularNivel(double chuva) {
        if (chuva > 60) return NivelRisco.CRITICO;
        if (chuva > 30) return NivelRisco.ALTO;
        if (chuva > 10) return NivelRisco.MEDIO;
        return NivelRisco.BAIXO;
    }

    private ZonaRisco findOrThrow(UUID id) {
        return zonaRiscoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona de risco nao encontrada: " + id));
    }

    private ZonaRiscoResponseDTO toResponse(ZonaRisco z) {
        Double lat = z.getLocalizacao() != null ? z.getLocalizacao().getLatitude() : null;
        Double lon = z.getLocalizacao() != null ? z.getLocalizacao().getLongitude() : null;
        return new ZonaRiscoResponseDTO(
                z.getId(), z.getNome(), z.getCidade(), z.getEstado(),
                lat, lon, z.getDescricao(), z.getNivelRiscoAtual(),
                z.getUltimaAnalise(), z.getDataCriacao());
    }
}
