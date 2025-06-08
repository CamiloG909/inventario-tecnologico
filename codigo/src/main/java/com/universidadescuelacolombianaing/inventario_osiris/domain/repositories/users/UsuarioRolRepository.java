package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.RolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {
	Set<UsuarioRolEntity> findAllByUsuario(UsuarioEntity usuario);
	Set<UsuarioRolEntity> findAllByRol(RolEntity rol);
}
