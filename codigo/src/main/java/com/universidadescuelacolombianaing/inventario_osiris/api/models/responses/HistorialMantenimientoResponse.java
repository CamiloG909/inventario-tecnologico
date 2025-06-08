package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialMantenimientoResponse {
	private Integer id;
	private LocalDate fecha;
	private String novedades;
	EncargadoMantenimientoResponse encargado;
	List<HistorialMantenimientoPreguntaResponse> preguntas;
}
