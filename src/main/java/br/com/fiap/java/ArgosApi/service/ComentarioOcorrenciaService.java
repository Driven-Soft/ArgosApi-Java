package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.ComentarioRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.ComentarioResponseDTO;
import br.com.fiap.java.ArgosApi.entity.ComentarioOcorrencia;
import br.com.fiap.java.ArgosApi.exception.ResourceNotFoundException;
import br.com.fiap.java.ArgosApi.repository.ComentarioOcorrenciaRepository;
import br.com.fiap.java.ArgosApi.repository.OcorrenciaRepository;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComentarioOcorrenciaService {

    private final ComentarioOcorrenciaRepository comentarioRepository;
    private final OcorrenciaRepository ocorrenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ComentarioResponseDTO> listarPorOcorrencia(UUID ocorrenciaId) {
        ocorrenciaRepository.findById(ocorrenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrencia nao encontrada"));
        return comentarioRepository.findByOcorrenciaIdOrderByDataCriacaoAsc(ocorrenciaId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public ComentarioResponseDTO criar(UUID ocorrenciaId, ComentarioRequestDTO dto) {
        var ocorrencia = ocorrenciaRepository.findById(ocorrenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrencia nao encontrada"));

        var comentario = ComentarioOcorrencia.builder()
                .mensagem(dto.mensagem())
                .ocorrencia(ocorrencia)
                .build();

        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        usuarioRepository.findByEmail(emailAutenticado).ifPresent(comentario::setUsuario);

        return toResponse(comentarioRepository.save(comentario));
    }

    private ComentarioResponseDTO toResponse(ComentarioOcorrencia c) {
        return new ComentarioResponseDTO(
                c.getId(), c.getMensagem(),
                c.getUsuario() != null ? c.getUsuario().getId() : null,
                c.getUsuario() != null ? c.getUsuario().getNome() : null,
                c.getOcorrencia() != null ? c.getOcorrencia().getId() : null,
                c.getDataCriacao());
    }
}
