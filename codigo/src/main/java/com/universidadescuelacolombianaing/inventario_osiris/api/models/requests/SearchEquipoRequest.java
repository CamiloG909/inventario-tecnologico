package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchEquipoRequest {
	@NotBlank(message = "El campo de búsqueda es obligatorio")
	private String field;

	@NotBlank(message = "El valor de búsqueda es obligatorio")
	private String value;
}
