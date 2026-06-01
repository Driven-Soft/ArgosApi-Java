package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String nome;
    private String cidade;
    private String estado;
    private Double latitude;
    private Double longitude;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private NivelRisco nivelRiscoAtual;

    private Double indiceSusceptibilidade;
    private Double indiceHistoricoRisco;
    private LocalDateTime ultimaAnalise;
    private boolean ativa = true;
    private LocalDateTime dataCriacao;
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "zonaRisco", cascade = CascadeType.ALL)
    private List<Alerta> alertas;

    @OneToMany(mappedBy = "zonaRisco", cascade = CascadeType.ALL)
    private List<AnaliseRisco> analises;

    @OneToMany(mappedBy = "zonaRisco", cascade = CascadeType.ALL)
    private List<Ocorrencia> ocorrencias;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        ativa = true;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
