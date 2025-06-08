package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<RolEntity, Integer> {
	RolEntity findByDescripcion(String descripcion);
}
