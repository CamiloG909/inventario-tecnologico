package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcaRepository extends JpaRepository<MarcaEntity, Integer> {
	Optional<MarcaEntity> findByDescripcionContainingIgnoreCase(String descripcion);
}
