package br.com.fiap.java.ArgosApi.controller;

import br.com.fiap.java.ArgosApi.dto.request.AuthRequestDTO;
import br.com.fiap.java.ArgosApi.dto.request.RegisterRequestDTO;
import br.com.fiap.java.ArgosApi.dto.response.AuthResponseDTO;
import br.com.fiap.java.ArgosApi.dto.response.UsuarioResponseDTO;
import br.com.fiap.java.ArgosApi.security.JwtUtil;
import br.com.fiap.java.ArgosApi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Registro e login de usuarios")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuario")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login e obtencao de token JWT")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        String token = jwtUtil.generateToken(request.email());
        return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer"));
    }
}
