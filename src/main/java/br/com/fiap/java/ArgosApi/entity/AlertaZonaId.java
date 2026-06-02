package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AlertaZonaId implements Serializable {

    @Column(name = "zona_risco_id")
    private UUID zonaRiscoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_alerta")
    private NivelAlerta nivelAlerta;
}
