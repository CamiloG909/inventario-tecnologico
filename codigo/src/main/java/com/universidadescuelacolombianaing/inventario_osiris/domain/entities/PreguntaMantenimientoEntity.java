package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "pregunta_mantenimiento")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PreguntaMantenimientoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private String pregunta;
	@Column(length = 1, nullable = false)
	private Integer obligatoria;
	@Column(length = 1, nullable = false)
	private Integer estado;
}
