package br.com.fiap.java.ArgosApi.repository;

import br.com.fiap.java.ArgosApi.entity.ZonaRisco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ZonaRiscoRepository extends JpaRepository<ZonaRisco, UUID> {}
