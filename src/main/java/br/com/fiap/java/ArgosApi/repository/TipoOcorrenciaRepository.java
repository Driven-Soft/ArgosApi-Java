package br.com.fiap.java.ArgosApi.repository;

import br.com.fiap.java.ArgosApi.entity.TipoOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TipoOcorrenciaRepository extends JpaRepository<TipoOcorrencia, UUID> {
}
