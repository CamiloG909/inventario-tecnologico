package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventarioSinDetallesVwResponse {
	private Integer id;
	private String marca;
	private String modelo;
	private String tipo_equipo;
	private String serial;
	private String placa;
	private String numero;
	private Integer datos_adicionales;
	private String observaciones;
	private String responsable_nombre;
	private String estado;
	private String fecha_actualizacion;
	private Integer id_tipo;
	private String mantenimiento;
}
