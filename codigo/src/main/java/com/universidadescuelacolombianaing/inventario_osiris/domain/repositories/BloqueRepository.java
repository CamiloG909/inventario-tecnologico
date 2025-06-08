package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.BloqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface BloqueRepository  extends JpaRepository<BloqueEntity, Integer> {
	Optional<BloqueEntity> findByDescripcion(String descripcion);
}
