package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analises_risco")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnaliseRisco {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "zona_risco_id")
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
