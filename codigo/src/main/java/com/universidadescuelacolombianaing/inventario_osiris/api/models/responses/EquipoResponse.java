package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipoResponse {
	private Integer id;
	private String serial;
	private String placa;
	private String observaciones;
	private String numero;
	private Integer datos_adicionales;
	private String fecha_actualizacion;
	private Integer id_marca;
	private Integer id_modelo;
	private Integer id_estado;
	private Integer id_detalles_equipo;
	private Integer id_responsable;
}
