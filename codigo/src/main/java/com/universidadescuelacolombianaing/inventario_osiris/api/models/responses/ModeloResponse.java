package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ModeloResponse {
	private Integer id;
	private Integer id_tipo;
	private String descripcion;
}
