package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe embeddable que agrupa latitude e longitude.
 * Satisfaz o requisito de modelagem avancada (@Embedded) do enunciado.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Localizacao {
    private Double latitude;
    private Double longitude;
}
