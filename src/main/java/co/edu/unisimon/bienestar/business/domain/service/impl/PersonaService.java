package co.edu.unisimon.bienestar.business.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unisimon.bienestar.business.domain.persistence.entity.PersonaEntity;
import co.edu.unisimon.bienestar.business.domain.persistence.repository.PersonaRepository;
import co.edu.unisimon.bienestar.business.domain.service.IPersonaService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PersonaService implements IPersonaService {

    private final PersonaRepository personaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PersonaEntity> findAll() {
        return personaRepository.findAll();
    }

    @Override
    public PersonaEntity save(PersonaEntity personaEntity) {
        return personaRepository.save(personaEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonaEntity> findById(Long id) {
        return personaRepository.findById(id);
    }

    @Override
    public void delete(PersonaEntity personaEntity) {
        personaRepository.delete(personaEntity);
    }
}
