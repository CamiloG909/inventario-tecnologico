package com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "usuario_rol")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuarioRolEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private UsuarioEntity usuario;

	@ManyToOne
	@JoinColumn(name = "id_rol")
	private RolEntity rol;
}
