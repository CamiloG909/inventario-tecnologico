package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "equipo")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String serial;
	private String placa;
	private String numero;
	private Integer datos_adicionales;
	private String observaciones;
	private String fecha_actualizacion;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "equipo",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true)
	private Set<HistorialEntity> historialEquipos;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "equipo",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true)
	private Set<HistorialMantenimientoEntity> mantenimientos;

	@ManyToOne
	@JoinColumn(name = "id_marca")
	private MarcaEntity marca;

	@ManyToOne
	@JoinColumn(name = "id_modelo")
	private ModeloEntity modelo;

	@ManyToOne
	@JoinColumn(name = "id_estado")
	private EstadoEntity estado;

	@ManyToOne
	@JoinColumn(name = "id_detalles_equipo")
	private DetallesEntity detalles;

	@ManyToOne
	@JoinColumn(name = "id_responsable")
	private ResponsableEntity responsable;
}
