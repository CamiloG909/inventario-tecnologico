package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.EquipoEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.HistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface HistorialRepository extends JpaRepository<HistorialEntity, Integer> {
	@Query("SELECT t FROM historial_equipo t WHERE t.equipo = ?1 AND t.actualizacion_datos = 0 ORDER BY t.id DESC")
	Set<HistorialEntity> findAllByEquipoByOrderIdValid(EquipoEntity equipo);

	@Query("SELECT t FROM historial_equipo t WHERE t.equipo = ?1 ORDER BY t.id DESC")
	Set<HistorialEntity> findAllByEquipoOrderById(EquipoEntity equipo);
}
