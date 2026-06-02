package br.com.fiap.java.ArgosApi.service;

import br.com.fiap.java.ArgosApi.dto.request.RegisterRequestDTO;
import br.com.fiap.java.ArgosApi.dto.request.UsuarioRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.UsuarioResponseDTO;
import br.com.fiap.java.ArgosApi.entity.Usuario;
import br.com.fiap.java.ArgosApi.exception.ResourceNotFoundException;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO registrar(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email ja cadastrado: " + dto.email());
        }
        var u = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senhaHash(passwordEncoder.encode(dto.senha()))
                .telefone(dto.telefone())
                .build();
        return toResponse(usuarioRepository.save(u));
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        var u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
        return toResponse(u);
    }

    public UsuarioResponseDTO atualizar(String email, UsuarioRequestDTO dto) {
        var u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
        u.setNome(dto.nome());
        if (dto.telefone() != null) u.setTelefone(dto.telefone());
        return toResponse(usuarioRepository.save(u));
    }

    private UsuarioResponseDTO toResponse(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(),
                u.getTelefone(), u.getTipoUsuario());
    }
}
