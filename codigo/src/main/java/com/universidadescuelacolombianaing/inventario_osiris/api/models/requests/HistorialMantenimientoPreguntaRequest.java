package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialMantenimientoPreguntaRequest {
	@Positive(message = "El ID de la pregunta debe ser númerico")
	@NotNull(message = "El ID de la pregunta es obligatorio")
	private Integer id_pregunta;
	@NotNull(message = "Realizado es obligatorio")
	private Integer realizado;
	private String observaciones;
}
