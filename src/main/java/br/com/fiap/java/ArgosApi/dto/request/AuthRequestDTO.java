package br.com.fiap.java.ArgosApi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(@NotBlank String email, @NotBlank String senha) {
}
