package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import jakarta.validation.constraints.Email;
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
public class NotificacionEstadoRequest {
	@Positive(message = "El ID del estado debe ser númerico")
	@NotNull(message = "El ID del estado es obligatorio")
	private Integer id_estado;

	@Email(message = "El email no es válido")
	@NotBlank(message = "El email es obligatorio")
	private String email;
}
