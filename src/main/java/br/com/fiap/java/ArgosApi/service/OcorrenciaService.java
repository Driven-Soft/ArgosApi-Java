package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.OcorrenciaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.OcorrenciaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.*;
import br.com.fiap.java.ArgosApi.exception.ResourceNotFoundException;
import br.com.fiap.java.ArgosApi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;
    private final ZonaRiscoRepository zonaRiscoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<OcorrenciaResponseDTO> listarTodas() {
        return ocorrenciaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public OcorrenciaResponseDTO buscarPorId(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public OcorrenciaResponseDTO criar(OcorrenciaRequestDTO dto) {
        var tipo = tipoOcorrenciaRepository.findById(dto.tipoOcorrenciaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de ocorrencia nao encontrado"));
        var zona = zonaRiscoRepository.findById(dto.zonaRiscoId())
                .orElseThrow(() -> new ResourceNotFoundException("Zona de risco nao encontrada"));

        var o = Ocorrencia.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .tipoOcorrencia(tipo)
                .zonaRisco(zona)
                .localizacao(new Localizacao(dto.latitude(), dto.longitude()))
                .build();

        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        usuarioRepository.findByEmail(emailAutenticado).ifPresent(o::setUsuario);

        return toResponse(ocorrenciaRepository.save(o));
    }

    @Transactional
    public OcorrenciaResponseDTO atualizar(UUID id, OcorrenciaRequestDTO dto) {
        var o = findOrThrow(id);
        o.setTitulo(dto.titulo());
        o.setDescricao(dto.descricao());
        o.setLocalizacao(new Localizacao(dto.latitude(), dto.longitude()));
        tipoOcorrenciaRepository.findById(dto.tipoOcorrenciaId()).ifPresent(o::setTipoOcorrencia);
        zonaRiscoRepository.findById(dto.zonaRiscoId()).ifPresent(o::setZonaRisco);
        return toResponse(ocorrenciaRepository.save(o));
    }

    @Transactional
    public OcorrenciaResponseDTO alterarStatus(UUID id, StatusOcorrencia status) {
        var o = findOrThrow(id);
        o.setStatus(status);
        if (status == StatusOcorrencia.RESOLVIDA) o.setResolvidoEm(LocalDateTime.now());
        return toResponse(ocorrenciaRepository.save(o));
    }

    @Transactional
    public void deletar(UUID id) {
        findOrThrow(id);
        ocorrenciaRepository.deleteById(id);
    }

    private Ocorrencia findOrThrow(UUID id) {
        return ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrencia nao encontrada: " + id));
    }

    private OcorrenciaResponseDTO toResponse(Ocorrencia o) {
        Double lat = o.getLocalizacao() != null ? o.getLocalizacao().getLatitude() : null;
        Double lon = o.getLocalizacao() != null ? o.getLocalizacao().getLongitude() : null;
        return new OcorrenciaResponseDTO(
                o.getId(), o.getTitulo(), o.getDescricao(), o.getStatus(),
                o.getTipoOcorrencia() != null ? o.getTipoOcorrencia().getId() : null,
                o.getTipoOcorrencia() != null ? o.getTipoOcorrencia().getNome() : null,
                o.getZonaRisco() != null ? o.getZonaRisco().getId() : null,
                o.getZonaRisco() != null ? o.getZonaRisco().getNome() : null,
                o.getUsuario() != null ? o.getUsuario().getId() : null,
                lat, lon, o.getDataCriacao(), o.getResolvidoEm());
    }
}
