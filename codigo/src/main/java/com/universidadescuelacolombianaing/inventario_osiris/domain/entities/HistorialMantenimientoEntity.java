package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "historial_mantenimiento")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialMantenimientoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private LocalDate fecha;
	private String novedades;

	@ManyToOne
	@JoinColumn(name = "id_equipo")
	private EquipoEntity equipo;

	@ManyToOne
	@JoinColumn(name = "id_encargado_mantenimiento")
	private EncargadoMantenimientoEntity encargado;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "historial",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true)
	private Set<HistorialMantenimientoPreguntaEntity> preguntas;
}
