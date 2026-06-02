package br.com.fiap.java.ArgosApi.repository;

import br.com.fiap.java.ArgosApi.entity.AlertaZona;
import br.com.fiap.java.ArgosApi.entity.AlertaZonaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertaZonaRepository extends JpaRepository<AlertaZona, AlertaZonaId> {
    List<AlertaZona> findByZonaRiscoId(UUID zonaRiscoId);
}
