package br.com.fiap.java.ArgosApi.config;

import br.com.fiap.java.ArgosApi.entity.TipoOcorrencia;
import br.com.fiap.java.ArgosApi.entity.Usuario;
import br.com.fiap.java.ArgosApi.entity.TipoUsuario;
import br.com.fiap.java.ArgosApi.repository.TipoOcorrenciaRepository;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedTiposOcorrencia();
        seedUsuarioAdmin();
    }

    private void seedTiposOcorrencia() {
        List<TipoOcorrencia> tipos = List.of(
                TipoOcorrencia.builder().nome("Alagamento").descricao("Ocorrencia de alagamento ou enchente").build(),
                TipoOcorrencia.builder().nome("Deslizamento").descricao("Movimento de massa de terra ou deslizamento").build(),
                TipoOcorrencia.builder().nome("Arvore Caida").descricao("Queda de arvore ou obstrucao de via").build(),
                TipoOcorrencia.builder().nome("Incendio").descricao("Incendio em area urbana ou florestal").build(),
                TipoOcorrencia.builder().nome("Vendaval").descricao("Vento forte causando danos").build()
        );
        tipos.forEach(t -> tipoOcorrenciaRepository.findByNome(t.getNome())
                .orElseGet(() -> tipoOcorrenciaRepository.save(t)));
    }

    private void seedUsuarioAdmin() {
        usuarioRepository.findByEmail("admin@argos.com").orElseGet(() ->
                usuarioRepository.save(Usuario.builder()
                        .nome("Administrador")
                        .email("admin@argos.com")
                        .senhaHash(passwordEncoder.encode("admin123"))
                        .tipoUsuario(TipoUsuario.ADMIN)
                        .build())
        );
    }
}
