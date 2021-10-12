package co.edu.unisimon.bienestar.business.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unisimon.bienestar.business.domain.dto.TiposServicioDto;
import co.edu.unisimon.bienestar.business.domain.dto.TiposServiciosAreasDto;
import co.edu.unisimon.bienestar.business.domain.persistence.entity.TiposServiciosEntity;

@Mapper
public interface TiposServicioMapper {

	TiposServiciosEntity convertDtotoEntity(final TiposServicioDto tiposServicioDto);
	
	TiposServiciosAreasDto convertEntityToDto(final TiposServiciosEntity tiposServicioEntity);
	
	List<TiposServicioDto> convertEntityListToDtoList(
			final List<TiposServiciosEntity> tiposServiciosEntities);
			
	
	
	
	
}
