package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.EquipoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EquipoRepository extends JpaRepository<EquipoEntity, Integer> {
	Set<EquipoEntity> findAllBySerialIgnoreCase(String serial);
}
