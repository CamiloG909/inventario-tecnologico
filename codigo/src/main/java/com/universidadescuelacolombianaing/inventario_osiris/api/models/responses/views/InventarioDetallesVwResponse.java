package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventarioDetallesVwResponse {
	private Integer id;
	private String marca;
	private String modelo;
	private String tipo_equipo;
	private String serial;
	private String placa;
	private String numero;
	private Integer datos_adicionales;
	private String observaciones;
	private String estado;
	private String fecha_actualizacion;
	private String responsable_nombre;
	private String responsable_email;
	private String responsable_emplid;
	private String responsable_area;
	private String host;
	private String tipo_disco;
	private String capacidad_disco;
	private String capacidad_ram;
	private String procesador;
	private String os;
	private String mac_address;
	private Integer id_tipo;
	private String mantenimiento;
}
