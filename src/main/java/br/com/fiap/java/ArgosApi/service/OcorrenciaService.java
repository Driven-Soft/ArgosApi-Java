package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.OcorrenciaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.OcorrenciaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.*;
import br.com.fiap.java.ArgosApi.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;
    private final ZonaRiscoRepository zonaRiscoRepository;

    public OcorrenciaService(OcorrenciaRepository ocorrenciaRepository,
                             TipoOcorrenciaRepository tipoOcorrenciaRepository,
                             ZonaRiscoRepository zonaRiscoRepository) {
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.tipoOcorrenciaRepository = tipoOcorrenciaRepository;
        this.zonaRiscoRepository = zonaRiscoRepository;
    }

    public List<OcorrenciaResponseDTO> listarTodas() {
        return ocorrenciaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OcorrenciaResponseDTO buscarPorId(UUID id) {
        return ocorrenciaRepository.findById(id).map(this::toResponse).orElse(null);
    }

    public OcorrenciaResponseDTO criar(OcorrenciaRequestDTO dto) {
        Ocorrencia o = new Ocorrencia();
        o.setTitulo(dto.titulo());
        o.setDescricao(dto.descricao());
        o.setLatitude(dto.latitude());
        o.setLongitude(dto.longitude());
        o.setDataCriacao(LocalDateTime.now());

        tipoOcorrenciaRepository.findById(dto.tipoOcorrenciaId()).ifPresent(o::setTipoOcorrencia);
        zonaRiscoRepository.findById(dto.zonaRiscoId()).ifPresent(o::setZonaRisco);

        o = ocorrenciaRepository.save(o);
        return toResponse(o);
    }

    public OcorrenciaResponseDTO atualizar(UUID id, OcorrenciaRequestDTO dto) {
        return ocorrenciaRepository.findById(id).map(o -> {
            o.setTitulo(dto.titulo());
            o.setDescricao(dto.descricao());
            o.setLatitude(dto.latitude());
            o.setLongitude(dto.longitude());
            tipoOcorrenciaRepository.findById(dto.tipoOcorrenciaId()).ifPresent(o::setTipoOcorrencia);
            zonaRiscoRepository.findById(dto.zonaRiscoId()).ifPresent(o::setZonaRisco);
            ocorrenciaRepository.save(o);
            return toResponse(o);
        }).orElse(null);
    }

    public OcorrenciaResponseDTO alterarStatus(UUID id, StatusOcorrencia status) {
        return ocorrenciaRepository.findById(id).map(o -> {
            o.setStatus(status);
            if (status == StatusOcorrencia.RESOLVIDA) o.setResolvidoEm(LocalDateTime.now());
            ocorrenciaRepository.save(o);
            return toResponse(o);
        }).orElse(null);
    }

    private OcorrenciaResponseDTO toResponse(Ocorrencia o) {
        return new OcorrenciaResponseDTO(o.getId(), o.getTitulo(), o.getDescricao(), o.getStatus(), o.getDataCriacao());
    }
}
