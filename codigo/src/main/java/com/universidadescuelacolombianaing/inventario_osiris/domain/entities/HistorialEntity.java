package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "historial_equipo")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String ticket;
	private String observaciones;
	private String fecha_inicio;
	private String fecha_fin;
	private String punto_red;
	private Integer actualizacion_datos;
	private String email_autor;

	@ManyToOne
	@JoinColumn(name = "id_equipo")
	private EquipoEntity equipo;

	@ManyToOne
	@JoinColumn(name = "id_estado")
	private EstadoEntity estado;

	@ManyToOne
	@JoinColumn(name = "id_bloque")
	private BloqueEntity bloque;

	@ManyToOne
	@JoinColumn(name = "id_piso")
	private PisoEntity piso;

	@ManyToOne
	@JoinColumn(name = "id_ubicacion")
	private UbicacionEntity ubicacion;

	@ManyToOne
	@JoinColumn(name = "id_responsable")
	private ResponsableEntity responsable;
}
