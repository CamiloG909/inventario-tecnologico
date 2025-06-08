package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import jakarta.validation.constraints.NotBlank;
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
public class ModeloRequest {
	@NotBlank(message = "La descripción es obligatoria")
	private String descripcion;

	@Positive(message = "El ID del tipo debe ser númerico")
	@NotNull(message = "El ID del tipo es obligatorio")
	private Integer id_tipo;

}
