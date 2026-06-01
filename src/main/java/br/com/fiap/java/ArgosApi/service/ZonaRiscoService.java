package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.ZonaRiscoRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.ZonaRiscoResponseDTO;
import br.com.fiap.java.ArgosApi.entity.NivelRisco;
import br.com.fiap.java.ArgosApi.entity.ZonaRisco;
import br.com.fiap.java.ArgosApi.repository.ZonaRiscoRepository;
import br.com.fiap.java.ArgosApi.entity.AnaliseRisco;
import br.com.fiap.java.ArgosApi.repository.AnaliseRiscoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ZonaRiscoService {

    private final ZonaRiscoRepository zonaRiscoRepository;
    private final AnaliseRiscoRepository analiseRiscoRepository;
    private final RainMonitoringService rainMonitoringService;

    public ZonaRiscoService(ZonaRiscoRepository zonaRiscoRepository,
                            AnaliseRiscoRepository analiseRiscoRepository,
                            RainMonitoringService rainMonitoringService) {
        this.zonaRiscoRepository = zonaRiscoRepository;
        this.analiseRiscoRepository = analiseRiscoRepository;
        this.rainMonitoringService = rainMonitoringService;
    }

    public List<ZonaRiscoResponseDTO> listarTodas() {
        return zonaRiscoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ZonaRiscoResponseDTO buscarPorId(UUID id) {
        return zonaRiscoRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    public ZonaRiscoResponseDTO criar(ZonaRiscoRequestDTO dto) {
        ZonaRisco z = ZonaRisco.builder()
                .nome(dto.nome())
                .cidade(dto.cidade())
                .estado(dto.estado())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .descricao(dto.descricao())
                .build();

        z = zonaRiscoRepository.save(z);
        return toResponse(z);
    }

    public ZonaRiscoResponseDTO atualizar(UUID id, ZonaRiscoRequestDTO dto) {
        return zonaRiscoRepository.findById(id).map(z -> {
            z.setNome(dto.nome());
            z.setCidade(dto.cidade());
            z.setEstado(dto.estado());
            z.setLatitude(dto.latitude());
            z.setLongitude(dto.longitude());
            z.setDescricao(dto.descricao());
            zonaRiscoRepository.save(z);
            return toResponse(z);
        }).orElse(null);
    }

    public ZonaRiscoResponseDTO atualizarNivelRisco(UUID id, NivelRisco nivel) {
        return zonaRiscoRepository.findById(id).map(z -> {
            z.setNivelRiscoAtual(nivel);
            zonaRiscoRepository.save(z);
            return toResponse(z);
        }).orElse(null);
    }

    public Object analisarRisco(UUID id) {
        return zonaRiscoRepository.findById(id).map(z -> {
            double chuva = rainMonitoringService.getRainMm(z.getLatitude(), z.getLongitude());
            NivelRisco nivel;
            if (chuva > 60) nivel = NivelRisco.CRITICO;
            else if (chuva > 30) nivel = NivelRisco.ALTO;
            else if (chuva > 10) nivel = NivelRisco.MEDIO;
            else nivel = NivelRisco.BAIXO;

            var analise = AnaliseRisco.builder()
                    .zonaRisco(z)
                    .chuvaMm24h(chuva)
                    .scoreFinal((int) Math.min(100, chuva))
                    .nivelRisco(nivel)
                    .fonteDados("SIMULADO")
                    .build();

            analiseRiscoRepository.save(analise);

            z.setNivelRiscoAtual(nivel);
            z.setUltimaAnalise(java.time.LocalDateTime.now());
            zonaRiscoRepository.save(z);

            return java.util.Map.of(
                    "zonaId", z.getId(),
                    "chuva24h", chuva,
                    "score", analise.getScoreFinal(),
                    "nivelRisco", nivel
            );
        }).orElse(null);
    }

    private ZonaRiscoResponseDTO toResponse(ZonaRisco z) {
        return new ZonaRiscoResponseDTO(z.getId(), z.getNome(), z.getCidade(), z.getEstado(), z.getNivelRiscoAtual());
    }
}
