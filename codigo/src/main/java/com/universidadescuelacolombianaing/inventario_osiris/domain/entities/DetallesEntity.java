package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "detalles_equipo")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DetallesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String host;
	private String tipo_disco;
	private String capacidad_disco;
	private String capacidad_ram;
	private String procesador;
	private String os;
	private String mac_address;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "detalles",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY)
	private Set<EquipoEntity> equipos;
}
