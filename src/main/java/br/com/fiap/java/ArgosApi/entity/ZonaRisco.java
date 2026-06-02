package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "zonas_risco")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonaRisco {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    /** Localizacao geografica usando @Embedded - modelagem avancada */
    @Embedded
    private Localizacao localizacao;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NivelRisco nivelRiscoAtual = NivelRisco.BAIXO;

    private Double indiceSusceptibilidade;
    private Double indiceHistoricoRisco;
    private LocalDateTime ultimaAnalise;

    @Builder.Default
    private boolean ativa = true;

    private LocalDateTime dataCriacao;
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "zonaRisco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Alerta> alertas;

    @OneToMany(mappedBy = "zonaRisco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AnaliseRisco> analises;

    @OneToMany(mappedBy = "zonaRisco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ocorrencia> ocorrencias;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (nivelRiscoAtual == null) nivelRiscoAtual = NivelRisco.BAIXO;
        ativa = true;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
