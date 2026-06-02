package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ocorrencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ocorrencia {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ocorrencia_id")
    private TipoOcorrencia tipoOcorrencia;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusOcorrencia status = StatusOcorrencia.ABERTA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_risco_id")
    private ZonaRisco zonaRisco;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "ocorrencia_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "ocorrencia_longitude"))
    })
    private Localizacao localizacao;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime resolvidoEm;

    @OneToMany(mappedBy = "ocorrencia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ComentarioOcorrencia> comentarios;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (status == null) status = StatusOcorrencia.ABERTA;
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
