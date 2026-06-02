package br.com.fiap.java.ArgosApi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "tipos_ocorrencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoOcorrencia {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    @Builder.Default
    private boolean ativo = true;

    @PrePersist
    public void prePersist() {
        ativo = true;
    }
}
