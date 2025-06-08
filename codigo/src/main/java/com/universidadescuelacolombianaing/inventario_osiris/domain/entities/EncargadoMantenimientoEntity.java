package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "encargado_mantenimiento")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EncargadoMantenimientoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 100, nullable = false)
	private String nombre;
	@Column(length = 50, nullable = false)
	private String email;
	@Column(length = 100, nullable = false)
	private String empresa;
	@Column(length = 1, nullable = false)
	private Integer estado;
}
