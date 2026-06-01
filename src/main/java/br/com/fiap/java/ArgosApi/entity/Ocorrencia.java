package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String titulo;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "tipo_ocorrencia_id")
    private TipoOcorrencia tipoOcorrencia;

    @Enumerated(EnumType.STRING)
    private StatusOcorrencia status;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "zona_risco_id")
    private ZonaRisco zonaRisco;

    private Double latitude;
    private Double longitude;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private LocalDateTime resolvidoEm;

    @OneToMany(mappedBy = "ocorrencia", cascade = CascadeType.ALL)
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
