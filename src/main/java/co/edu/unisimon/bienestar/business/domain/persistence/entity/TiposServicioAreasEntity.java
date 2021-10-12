package co.edu.unisimon.bienestar.business.domain.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "tipos_servicio_areas", schema = "Bienestar")
@EqualsAndHashCode
@ToString
public class TiposServicioAreasEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "tipos_servicios_id", referencedColumnName = "id", nullable = false)
	private TiposServiciosEntity tipos_servicios;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "areas_bienestar_id",referencedColumnName = "id")
	private AreasBienestarEntity areas_bienestar;

	@Column(name = "sede_id", nullable = false)
	private Long sede_id;

	@Column(name = "estado", nullable = false)
	private Integer estado;

}
