package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private NivelAlerta nivelAlerta;

    @ManyToOne
    @JoinColumn(name = "zona_risco_id")
    private ZonaRisco zonaRisco;

    @ManyToOne
    @JoinColumn(name = "usuario_criador_id")
    private Usuario usuarioCriador;

    private LocalDateTime inicioVigencia;
    private LocalDateTime fimVigencia;
    private LocalDateTime dataCriacao;
    private boolean ativo = true;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        ativo = true;
    }
}
