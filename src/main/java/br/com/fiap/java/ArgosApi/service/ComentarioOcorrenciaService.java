package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.ComentarioRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.ComentarioResponseDTO;
import br.com.fiap.java.ArgosApi.entity.ComentarioOcorrencia;
import br.com.fiap.java.ArgosApi.repository.ComentarioOcorrenciaRepository;
import br.com.fiap.java.ArgosApi.repository.OcorrenciaRepository;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComentarioOcorrenciaService {

    private final ComentarioOcorrenciaRepository comentarioRepository;
    private final OcorrenciaRepository ocorrenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioOcorrenciaService(ComentarioOcorrenciaRepository comentarioRepository,
                                       OcorrenciaRepository ocorrenciaRepository,
                                       UsuarioRepository usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.ocorrenciaRepository = ocorrenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ComentarioResponseDTO> listarPorOcorrencia(UUID ocorrenciaId) {
        return comentarioRepository.findAll().stream()
                .filter(c -> c.getOcorrencia() != null && c.getOcorrencia().getId().equals(ocorrenciaId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ComentarioResponseDTO criar(UUID ocorrenciaId, ComentarioRequestDTO dto) {
        var comentario = new ComentarioOcorrencia();
        comentario.setMensagem(dto.mensagem());
        comentario.setDataCriacao(LocalDateTime.now());

        ocorrenciaRepository.findById(ocorrenciaId).ifPresent(comentario::setOcorrencia);
        usuarioRepository.findById(dto.usuarioId()).ifPresent(comentario::setUsuario);

        comentario = comentarioRepository.save(comentario);
        return toResponse(comentario);
    }

    private ComentarioResponseDTO toResponse(ComentarioOcorrencia c) {
        return new ComentarioResponseDTO(
                c.getId(),
                c.getMensagem(),
                c.getUsuario() != null ? c.getUsuario().getId() : null,
                c.getOcorrencia() != null ? c.getOcorrencia().getId() : null,
                c.getDataCriacao()
        );
    }
}
