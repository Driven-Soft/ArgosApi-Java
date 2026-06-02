package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas_zona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertaZona {

    @EmbeddedId
    private AlertaZonaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("zonaRiscoId")
    @JoinColumn(name = "zona_risco_id")
    private ZonaRisco zonaRisco;

    private String mensagemPadrao;
    private Integer limiarChuvaMm;

    @Builder.Default
    private boolean ativo = true;

    private LocalDateTime atualizadoEm;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
