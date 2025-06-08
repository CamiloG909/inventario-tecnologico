package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PreguntaMantenimientoResponse {
	private Integer id;
	private String pregunta;
	private Integer obligatoria;
	private Integer estado;
}
