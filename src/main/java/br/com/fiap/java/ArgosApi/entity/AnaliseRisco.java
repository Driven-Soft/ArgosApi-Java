package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analises_risco")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_analise", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BASICA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnaliseRisco {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_risco_id", nullable = false)
    private ZonaRisco zonaRisco;

    private Double chuvaMm24h;
    private Double floodProbability;
    private Double indiceSusceptibilidade;
    private Integer scoreFinal;

    @Enumerated(EnumType.STRING)
    private NivelRisco nivelRisco;

    private String fonteDados;
    private LocalDateTime dataAnalise;

    @PrePersist
    public void prePersist() {
        if (dataAnalise == null) dataAnalise = LocalDateTime.now();
    }
}
