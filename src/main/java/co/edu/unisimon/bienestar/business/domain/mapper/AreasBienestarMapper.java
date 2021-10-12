package co.edu.unisimon.bienestar.business.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unisimon.bienestar.business.domain.dto.AreasBienestarDto;
import co.edu.unisimon.bienestar.business.domain.persistence.entity.AreasBienestarEntity;

@Mapper
public interface AreasBienestarMapper {

	AreasBienestarEntity convertDtoToEntity(final AreasBienestarDto areasBienestarDto);
	
	AreasBienestarDto convertEntityToDto(final AreasBienestarEntity areasBienestarEntity);
	
	List<AreasBienestarDto> convertEntityListToDtoList(final List<AreasBienestarEntity> areasBienestarEntities);

	
	
	
	
}
