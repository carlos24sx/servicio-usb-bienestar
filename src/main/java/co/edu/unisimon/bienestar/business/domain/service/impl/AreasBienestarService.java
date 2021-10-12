package co.edu.unisimon.bienestar.business.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unisimon.bienestar.business.domain.persistence.entity.AreasBienestarEntity;
import co.edu.unisimon.bienestar.business.domain.persistence.repository.AreasBienestarRepository;
import co.edu.unisimon.bienestar.business.domain.service.IAreasBienestarService;
import lombok.AllArgsConstructor;


@Service
@Primary
@AllArgsConstructor
public class AreasBienestarService implements IAreasBienestarService {

	@Autowired
	AreasBienestarRepository repository;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<AreasBienestarEntity> findAll() {
	
		return repository.findAll();
	}

	@Override
	public AreasBienestarEntity save(AreasBienestarEntity t) {
	
		
		return repository.save(t);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<AreasBienestarEntity> findById(Long id) {
		
		return repository.findById(id);
	}

	@Override
	public void delete(AreasBienestarEntity t) {
		// TODO Auto-generated method stub

	}

}
