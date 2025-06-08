package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "historial_mantenimiento_pregunta")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialMantenimientoPreguntaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 1, nullable = false)
	private Integer realizado;
	private String observaciones;

	@ManyToOne
	@JoinColumn(name = "id_historial")
	private HistorialMantenimientoEntity historial;

	@ManyToOne
	@JoinColumn(name = "id_pregunta")
	private PreguntaMantenimientoEntity pregunta;
}
