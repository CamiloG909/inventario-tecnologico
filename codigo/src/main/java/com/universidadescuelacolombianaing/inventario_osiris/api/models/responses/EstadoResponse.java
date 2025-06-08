package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EstadoResponse {
	private Integer id;
	private String descripcion;
	private Integer admin;
	private Integer actualizar_responsable;
}
