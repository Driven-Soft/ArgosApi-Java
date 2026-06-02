package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelAlerta nivelAlerta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_risco_id", nullable = false)
    private ZonaRisco zonaRisco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_criador_id")
    private Usuario usuarioCriador;

    private LocalDateTime inicioVigencia;
    private LocalDateTime fimVigencia;
    private LocalDateTime dataCriacao;

    @Builder.Default
    private boolean ativo = true;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        ativo = true;
    }
}
