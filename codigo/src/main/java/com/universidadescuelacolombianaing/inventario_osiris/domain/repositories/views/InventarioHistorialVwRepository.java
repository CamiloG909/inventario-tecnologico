package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioHistorialVwEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface InventarioHistorialVwRepository extends JpaRepository<InventarioHistorialVwEntity, Integer> {
	@Query("SELECT t FROM inventario_historial t WHERE t.id_equipo = ?1")

	Set<InventarioHistorialVwEntity> selectByIdEquipo(Integer id);
}
