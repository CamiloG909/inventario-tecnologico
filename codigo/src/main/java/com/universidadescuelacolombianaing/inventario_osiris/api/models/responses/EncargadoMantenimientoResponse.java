package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EncargadoMantenimientoResponse {
	private Integer id;
	private String nombre;
	private String email;
	private String empresa;
	private Integer estado;
}
