package com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "rol")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RolEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String descripcion;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "rol",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY,
		orphanRemoval = true)
	private Set<UsuarioRolEntity> usuariosRol;
}
