package com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "inventario_ultreg_historial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventarioHistorialUltimoRegistroVwEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer id_equipo;
	private String bloque;
	private String piso;
	private String ubicacion;
	private String tipo_ubicacion;
	private String responsable_emplid;
	private String responsable_email;
	private String responsable_nombre;
	private String responsable_area;
	private String estado;
	private String fecha_inicio;
	private String fecha_fin;
	private String ticket;
	private String punto_red;
	private String observaciones;
	private Integer actualizacion_datos;
	private String email_autor;
}
