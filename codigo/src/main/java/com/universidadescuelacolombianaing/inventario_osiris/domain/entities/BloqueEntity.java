package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "bloque")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BloqueEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String descripcion;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "bloque",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY)
	private Set<HistorialEntity> historialEquipos;
}
