package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialMantenimientoRequest {
	@NotBlank(message = "La fecha es obligatoria")
	private String fecha;
	private String novedades;
	@Positive(message = "El ID del encargado debe ser númerico")
	@NotNull(message = "El ID del encargado es obligatorio")
	private Integer id_encargado_mantenimiento;
	@NotNull(message = "Las preguntas son obligatorias")
	@Size(min = 1, message = "Al menos una pregunta")
	@Valid
	private List<HistorialMantenimientoPreguntaRequest> preguntas;
}
