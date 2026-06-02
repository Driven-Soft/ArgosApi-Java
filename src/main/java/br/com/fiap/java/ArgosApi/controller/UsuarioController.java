package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.UsuarioRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.UsuarioResponseDTO;
import br.com.fiap.java.ArgosApi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gerenciamento do perfil do usuario autenticado")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    @Operation(summary = "Retorna dados do usuario autenticado")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication auth) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(auth.getName()));
    }

    @PutMapping("/me")
    @Operation(summary = "Atualiza nome e telefone do usuario autenticado")
    public ResponseEntity<UsuarioResponseDTO> atualizar(Authentication auth,
                                                         @RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizar(auth.getName(), dto));
    }
}
