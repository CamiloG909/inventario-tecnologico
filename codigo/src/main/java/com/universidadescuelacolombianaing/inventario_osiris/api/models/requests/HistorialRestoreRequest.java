package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialRestoreRequest {
	private String observaciones;
}
