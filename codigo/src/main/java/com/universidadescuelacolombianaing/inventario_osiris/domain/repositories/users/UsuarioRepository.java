package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
	Set<UsuarioEntity> findAllByEmail(String email);
}
