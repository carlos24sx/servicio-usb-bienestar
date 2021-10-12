package co.edu.unisimon.bienestar.business.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TiposServiciosAreasDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotNull
	private TiposServicioDto tipoServicio;
	
	@NotNull
	private AreasBienestarDto areasBienestar;
	
	
}
