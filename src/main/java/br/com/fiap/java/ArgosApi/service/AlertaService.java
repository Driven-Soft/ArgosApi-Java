package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.AlertaRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AlertaResponseDTO;
import br.com.fiap.java.ArgosApi.entity.Alerta;
import br.com.fiap.java.ArgosApi.repository.AlertaRepository;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import br.com.fiap.java.ArgosApi.repository.ZonaRiscoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final ZonaRiscoRepository zonaRiscoRepository;
    private final UsuarioRepository usuarioRepository;

    public AlertaService(AlertaRepository alertaRepository, ZonaRiscoRepository zonaRiscoRepository, UsuarioRepository usuarioRepository) {
        this.alertaRepository = alertaRepository;
        this.zonaRiscoRepository = zonaRiscoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<AlertaResponseDTO> listarTodos() {
        return alertaRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AlertaResponseDTO buscarPorId(UUID id) {
        return alertaRepository.findById(id).map(this::toResponse).orElse(null);
    }

    public AlertaResponseDTO criar(AlertaRequestDTO dto) {
        Alerta a = Alerta.builder()
                .titulo(dto.titulo())
                .descricao(dto.descricao())
                .nivelAlerta(dto.nivelAlerta())
                .inicioVigencia(dto.inicioVigencia())
                .fimVigencia(dto.fimVigencia())
                .dataCriacao(LocalDateTime.now())
                .ativo(true)
                .build();

        zonaRiscoRepository.findById(dto.zonaRiscoId()).ifPresent(a::setZonaRisco);
        usuarioRepository.findById(dto.usuarioCriadorId()).ifPresent(a::setUsuarioCriador);

        a = alertaRepository.save(a);
        return toResponse(a);
    }

    public AlertaResponseDTO atualizar(UUID id, AlertaRequestDTO dto) {
        return alertaRepository.findById(id).map(a -> {
            a.setTitulo(dto.titulo());
            a.setDescricao(dto.descricao());
            a.setNivelAlerta(dto.nivelAlerta());
            a.setInicioVigencia(dto.inicioVigencia());
            a.setFimVigencia(dto.fimVigencia());
            zonaRiscoRepository.findById(dto.zonaRiscoId()).ifPresent(a::setZonaRisco);
            usuarioRepository.findById(dto.usuarioCriadorId()).ifPresent(a::setUsuarioCriador);
            alertaRepository.save(a);
            return toResponse(a);
        }).orElse(null);
    }

    public AlertaResponseDTO alterarStatus(UUID id, boolean ativo) {
        return alertaRepository.findById(id).map(a -> {
            a.setAtivo(ativo);
            alertaRepository.save(a);
            return toResponse(a);
        }).orElse(null);
    }

    private AlertaResponseDTO toResponse(Alerta a) {
        return new AlertaResponseDTO(a.getId(), a.getTitulo(), a.getDescricao(), a.getNivelAlerta(), a.getDataCriacao(), a.isAtivo());
    }
}
