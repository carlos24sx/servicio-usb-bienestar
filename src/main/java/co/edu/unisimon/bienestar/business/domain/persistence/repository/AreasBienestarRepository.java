package co.edu.unisimon.bienestar.business.domain.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unisimon.bienestar.business.domain.persistence.entity.AreasBienestarEntity;

@Repository
public interface AreasBienestarRepository extends JpaRepository<AreasBienestarEntity, Long>{

}
