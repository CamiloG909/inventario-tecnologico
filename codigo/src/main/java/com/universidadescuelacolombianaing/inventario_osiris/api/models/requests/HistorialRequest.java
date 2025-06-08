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
public class HistorialRequest {
	@Positive(message = "El ID del estado debe ser númerico")
	@NotNull(message = "El ID del estado es obligatorio")
	private Integer id_estado;
	@Positive(message = "El ID del responsable debe ser númerico")
	@NotNull(message = "El ID del responsable es obligatorio")
	private Integer id_responsable;
	private boolean change_location;
	@Positive(message = "El ID del bloque debe ser númerico")
	private Integer id_bloque;
	@Positive(message = "El ID del piso debe ser númerico")
	private Integer id_piso;
	@Positive(message = "El ID de la ubicación debe ser númerico")
	private Integer id_ubicacion;
	private String fecha_inicio;
	private String fecha_fin;
	private String ticket;
	private String punto_red;
	private String observaciones;
}
