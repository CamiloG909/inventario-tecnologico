package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.ModeloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeloRepository extends JpaRepository<ModeloEntity, Integer> {
	Optional<ModeloEntity> findByDescripcionContainingIgnoreCase(String descripcion);
}
