package br.com.fiap.java.ArgosApi.config;

import br.com.fiap.java.ArgosApi.entity.TipoOcorrencia;
import br.com.fiap.java.ArgosApi.repository.TipoOcorrenciaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;

    public DataInitializer(TipoOcorrenciaRepository tipoOcorrenciaRepository) {
        this.tipoOcorrenciaRepository = tipoOcorrenciaRepository;
    }

    @Override
    public void run(String... args) {
        List<TipoOcorrencia> tipos = List.of(
                TipoOcorrencia.builder().nome("Alagamento").descricao("Ocorrencia de alagamento ou enchente").build(),
                TipoOcorrencia.builder().nome("Deslizamento").descricao("Movimento de massa de terra ou deslizamento").build(),
                TipoOcorrencia.builder().nome("Árvore caída").descricao("Queda de arvore ou obstrucao de via").build(),
                TipoOcorrencia.builder().nome("Incêndio").descricao("Incendio em area urbana ou florestal").build()
        );

        tipos.forEach(tipo ->
                tipoOcorrenciaRepository.findByNome(tipo.getNome())
                        .orElseGet(() -> tipoOcorrenciaRepository.save(tipo))
        );
    }
}
