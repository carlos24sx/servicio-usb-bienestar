package co.edu.unisimon.bienestar.business.domain.dto;

import lombok.Setter;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AreasBienestarDto implements Serializable {
	 
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotNull
	private String nombre;
	
	
	private String descripcion;
	
	
}
