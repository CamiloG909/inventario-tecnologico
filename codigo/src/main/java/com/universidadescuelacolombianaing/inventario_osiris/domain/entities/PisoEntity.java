package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "piso")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PisoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String descripcion;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "piso",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY)
	private Set<HistorialEntity> historialEquipos;
}
