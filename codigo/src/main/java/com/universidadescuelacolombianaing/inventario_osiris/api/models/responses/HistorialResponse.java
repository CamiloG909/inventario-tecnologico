package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistorialResponse {
	private Integer id;
	private String ticket;
	private String observaciones;
	private String fecha_inicio;
	private String fecha_fin;
	private String punto_red;
	private Integer actualizacion_datos;
	private String email_autor;
	private Integer id_equipo;
	private Integer id_estado;
	private Integer id_bloque;
	private Integer id_piso;
	private Integer id_ubicacion;
	private Integer id_responsable;
}
