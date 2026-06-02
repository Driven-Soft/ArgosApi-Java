package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DETALHADA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AnaliseRiscoDetalhada extends AnaliseRisco {

    private String responsavelTecnico;
    private String observacoesCampo;
    private Double temperaturaC;
    private Double umidadeRelativa;
    private Boolean evacuacaoRecomendada;
}
