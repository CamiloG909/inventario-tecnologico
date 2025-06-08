package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MantenimientoReporteResponse {
	private Integer id;
	private String tipo;
	private String marca;
	private String modelo;
	private String serial;
	private String placa;
	private String numero;
	private String host;
	private String responsable;
	private String bloque;
	private String piso;
	private String ubicacion;
	private LocalDate fecha;
	private String novedades_mantenimiento;
	private String encargado_mantenimiento;
	private String pregunta;
	private Integer realizado;
	private String observaciones;
}
