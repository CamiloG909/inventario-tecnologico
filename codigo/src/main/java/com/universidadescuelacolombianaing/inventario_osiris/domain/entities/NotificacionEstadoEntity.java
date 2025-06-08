package com.universidadescuelacolombianaing.inventario_osiris.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "notificacion_estado")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NotificacionEstadoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String email;

	@ManyToOne
	@JoinColumn(name = "id_estado")
	private EstadoEntity estado;
}
