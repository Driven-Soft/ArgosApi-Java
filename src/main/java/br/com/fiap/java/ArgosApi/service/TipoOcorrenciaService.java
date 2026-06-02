package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.response.TipoOcorrenciaResponseDTO;
import br.com.fiap.java.ArgosApi.repository.TipoOcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoOcorrenciaService {

    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;

    public List<TipoOcorrenciaResponseDTO> listarTodos() {
        return tipoOcorrenciaRepository.findAll().stream()
                .filter(t -> t.isAtivo())
                .map(t -> new TipoOcorrenciaResponseDTO(t.getId(), t.getNome(), t.getDescricao()))
                .toList();
    }
}
