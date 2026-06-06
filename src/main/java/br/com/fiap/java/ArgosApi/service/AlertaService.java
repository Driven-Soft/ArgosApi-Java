package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.AlertaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AlertaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.Alerta;
import br.com.fiap.java.ArgosApi.exception.ResourceNotFoundException;
import br.com.fiap.java.ArgosApi.repository.AlertaRepository;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import br.com.fiap.java.ArgosApi.repository.ZonaRiscoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final ZonaRiscoRepository zonaRiscoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<AlertaResponseDTO> listarTodos() {
        return alertaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AlertaResponseDTO buscarPorId(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public AlertaResponseDTO criar(AlertaRequestDTO dto) {
        var zona = zonaRiscoRepository.findById(dto.zonaRiscoId())
                .orElseThrow(() -> new ResourceNotFoundException("Zona de risco nao encontrada"));

        var a = Alerta.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .nivelAlerta(dto.nivelAlerta())
                .zonaRisco(zona)
                .inicioVigencia(dto.inicioVigencia())
                .fimVigencia(dto.fimVigencia())
                .build();

        if (dto.usuarioCriadorId() != null) {
            usuarioRepository.findById(dto.usuarioCriadorId()).ifPresent(a::setUsuarioCriador);
        }

        return toResponse(alertaRepository.save(a));
    }

    @Transactional
    public AlertaResponseDTO atualizar(UUID id, AlertaRequestDTO dto) {
        var a = findOrThrow(id);
        a.setTitulo(dto.titulo());
        a.setDescricao(dto.descricao());
        a.setNivelAlerta(dto.nivelAlerta());
        a.setInicioVigencia(dto.inicioVigencia());
        a.setFimVigencia(dto.fimVigencia());
        zonaRiscoRepository.findById(dto.zonaRiscoId()).ifPresent(a::setZonaRisco);
        if (dto.usuarioCriadorId() != null) {
            usuarioRepository.findById(dto.usuarioCriadorId()).ifPresent(a::setUsuarioCriador);
        }
        return toResponse(alertaRepository.save(a));
    }

    @Transactional
    public AlertaResponseDTO alterarStatus(UUID id, boolean ativo) {
        var a = findOrThrow(id);
        a.setAtivo(ativo);
        return toResponse(alertaRepository.save(a));
    }

    @Transactional
    public void deletar(UUID id) {
        findOrThrow(id);
        alertaRepository.deleteById(id);
    }

    private Alerta findOrThrow(UUID id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta nao encontrado: " + id));
    }

    private AlertaResponseDTO toResponse(Alerta a) {
        return new AlertaResponseDTO(
                a.getId(), a.getTitulo(), a.getDescricao(), a.getNivelAlerta(),
                a.getZonaRisco() != null ? a.getZonaRisco().getId() : null,
                a.getZonaRisco() != null ? a.getZonaRisco().getNome() : null,
                a.getInicioVigencia(), a.getFimVigencia(), a.getDataCriacao(), a.isAtivo());
    }
}
