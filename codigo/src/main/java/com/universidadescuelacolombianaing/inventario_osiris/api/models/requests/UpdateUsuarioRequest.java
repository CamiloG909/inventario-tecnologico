package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateUsuarioRequest {
	@Positive(message = "El ID del rol debe ser númerico")
	@NotNull(message = "El ID del rol es obligatorio")
	private Integer id_rol;

	@Email(message = "El email no es válido")
	@NotBlank(message = "El email es obligatorio")
	private String email;
}
