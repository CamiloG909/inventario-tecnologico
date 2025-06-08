package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "pregunta_mantenimiento_tipo")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PreguntaMantenimientoTipoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_pregunta")
	private PreguntaMantenimientoEntity pregunta;

	@ManyToOne
	@JoinColumn(name = "id_tipo")
	private TipoEntity tipo;
}
