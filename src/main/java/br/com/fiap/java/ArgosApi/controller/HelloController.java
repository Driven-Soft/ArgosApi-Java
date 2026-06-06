package br.com.fiap.java.ArgosApi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "Status", description = "Informacoes gerais da API")
public class HelloController {

    @GetMapping("/")
    @Operation(summary = "Redireciona para /status")
    public void root(HttpServletResponse response) throws IOException {
        response.sendRedirect("/status");
    }

    @GetMapping("/status")
    @Operation(summary = "Status e informacoes gerais da API")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "api", "Argos API",
                "versao", "v1.0",
                "status", "online",
                "timestamp", LocalDateTime.now().toString(),
                "swagger", "/swagger-ui.html",
                "docs", "/v3/api-docs",
                "descricao", "API REST para monitoramento de zonas de risco e desastres naturais"
        ));
    }

    @GetMapping("/hello")
    @Operation(summary = "Health check simples")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of(
                "mensagem", "Seja bem-vindo(a) ao Argos API!",
                "status", "online"
        ));
    }
}