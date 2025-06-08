package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "modelo")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ModeloEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String descripcion;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "modelo",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY,
		orphanRemoval = true)
	private Set<EquipoEntity> equipos;

	@ManyToOne
	@JoinColumn(name = "id_tipo")
	private TipoEntity tipo;
}
