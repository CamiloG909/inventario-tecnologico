package com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuarioEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String email;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "usuario",
		cascade = CascadeType.ALL,
		fetch = FetchType.EAGER,
		orphanRemoval = true)
	private Set<UsuarioRolEntity> usuariosRol;
}
