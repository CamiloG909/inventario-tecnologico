package com.universidadescuelacolombianaing.inventario_osiris.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DetallesRequest {
	private String host;
	private String procesador;
	private String os;
	private String tipo_disco;
	private String capacidad_disco;
	private String capacidad_ram;
	private String mac_address;
}
