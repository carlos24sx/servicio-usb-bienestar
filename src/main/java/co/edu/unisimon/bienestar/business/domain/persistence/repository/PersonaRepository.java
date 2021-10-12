package co.edu.unisimon.bienestar.business.domain.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unisimon.bienestar.business.domain.persistence.entity.PersonaEntity;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {



}
