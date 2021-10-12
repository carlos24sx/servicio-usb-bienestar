package co.edu.unisimon.bienestar.business.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDto implements Serializable {

    private static final long serialVersionUID = -5110703908162431896L;
    public static final String NAME = "Persona";

    private Long id;
    private String nombre;
    private String apellido;

}
