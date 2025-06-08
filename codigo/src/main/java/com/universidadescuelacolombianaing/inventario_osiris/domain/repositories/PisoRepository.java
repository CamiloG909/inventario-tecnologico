package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.PisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PisoRepository  extends JpaRepository<PisoEntity, Integer> {
	Optional<PisoEntity> findByDescripcion(String descripcion);
}
