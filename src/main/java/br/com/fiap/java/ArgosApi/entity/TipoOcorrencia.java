package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "tipos_ocorrencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoOcorrencia {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String nome;
    private String descricao;
    private boolean ativo = true;

    @PrePersist
    public void prePersist() {
        ativo = true;
    }
}
