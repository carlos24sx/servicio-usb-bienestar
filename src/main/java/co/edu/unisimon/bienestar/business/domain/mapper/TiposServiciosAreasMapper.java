package co.edu.unisimon.bienestar.business.domain.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.unisimon.bienestar.business.domain.dto.TiposServiciosAreasDto;
import co.edu.unisimon.bienestar.business.domain.persistence.entity.TiposServicioAreasEntity;

@Mapper(uses = {TiposServicioMapper.class, AreasBienestarMapper.class})
public interface TiposServiciosAreasMapper {

	TiposServicioAreasEntity convertDtoToEntity(final TiposServiciosAreasDto tipoServicioAreasDto);
	
	TiposServiciosAreasDto convertEntityToDto(final TiposServicioAreasEntity tiposServicioAreasEntity);
	
	List<TiposServiciosAreasDto> convertEntityListToDtoList(final List<TiposServicioAreasEntity> tiposServicioAreasEntities);
	
	
}
