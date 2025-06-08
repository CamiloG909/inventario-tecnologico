package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioRolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.UsuarioRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.UsuarioRolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.utils.InventoryUtil;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.NoPermissionsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
public class InvalidateUserRol {
	private final UsuarioRepository usuarioRepository;
	private final UsuarioRolRepository usuarioRolRepository;

	public String invalidate(String email, Integer rolRequired) {
		// ROL 0: MANTENIMIENTO 1:TECNICO 2:ADMIN 3:SUPERADMIN
		String emailDecoded;

		try {
			emailDecoded = InventoryUtil.decryptEmail(email);
		} catch (RuntimeException e) {
			throw new NoPermissionsException();
		}

		Set<UsuarioEntity> foundUsuarios = this.usuarioRepository.findAllByEmail(emailDecoded);
		if (foundUsuarios.size() != 1) throw new NoPermissionsException();

		UsuarioEntity usuario = foundUsuarios.stream().toList().get(0);

		Set<UsuarioRolEntity> foundRol = this.usuarioRolRepository.findAllByUsuario(usuario);
		if (foundRol.size() != 1) throw new NoPermissionsException();

		Integer idRol = foundRol.stream().toList().get(0).getRol().getId();

		boolean validRol = rolRequired == 3 ? idRol.equals(1) :
			rolRequired == 2 ? idRol.equals(1) || idRol.equals(2) :
				rolRequired == 1 ? (idRol.equals(1) || idRol.equals(2) || idRol.equals(3)):
				rolRequired == 0 && (idRol.equals(1) || idRol.equals(2) || idRol.equals(3)|| idRol.equals(4))
				;

		if (!validRol) throw new NoPermissionsException();

		return emailDecoded;
	}
}
