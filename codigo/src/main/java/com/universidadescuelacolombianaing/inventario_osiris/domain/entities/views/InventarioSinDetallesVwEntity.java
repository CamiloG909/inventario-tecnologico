package com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "inventario_sin_detalles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventarioSinDetallesVwEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
