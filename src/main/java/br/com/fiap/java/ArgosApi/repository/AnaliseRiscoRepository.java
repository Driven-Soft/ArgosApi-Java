package br.com.fiap.java.ArgosApi.repository;

import br.com.fiap.java.ArgosApi.entity.AnaliseRisco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnaliseRiscoRepository extends JpaRepository<AnaliseRisco, UUID> {
    List<AnaliseRisco> findByZonaRiscoIdOrderByDataAnaliseDesc(UUID zonaRiscoId);
}
