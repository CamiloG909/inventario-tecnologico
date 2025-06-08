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
public class EquipoRequest {
	@Positive(message = "El ID de la marca debe ser númerico")
	@NotNull(message = "El ID de la marca es obligatorio")
	private Integer id_marca;

	@Positive(message = "El ID del modelo debe ser númerico")
	@NotNull(message = "El ID del modelo es obligatorio")
	private Integer id_modelo;

	@Positive(message = "El ID del estado debe ser númerico")
	@NotNull(message = "El ID del estado es obligatorio")
	private Integer id_estado;

	@Positive(message = "El ID del responsable debe ser númerico")
	@NotNull(message = "El ID del responsable es obligatorio")
	private Integer id_responsable;

	@NotBlank(message = "El serial es obligatorio")
	private String serial;
	private String placa;
	private String observaciones;
	private String numero;

	@Max(value = 1, message = "Los datos adicionales debe ser 1 o 0")
	@Min(value = 0, message = "Los datos adicionales debe ser 1 o 0")
	@NotNull(message = "Los datos adicionales son obligatorios")
	private Integer datos_adicionales;

	private DetallesRequest detalles;
	@Positive(message = "El ID del bloque debe ser númerico")
	private Integer id_bloque;
	@Positive(message = "El ID del piso debe ser númerico")
	private Integer id_piso;
	@Positive(message = "El ID de la ubicación debe ser númerico")
	private Integer id_ubicacion;
}
