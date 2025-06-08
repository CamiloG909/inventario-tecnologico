package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.DetallesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetallesRepository extends JpaRepository<DetallesEntity, Integer> {
	@Query("SELECT DISTINCT d.capacidad_disco FROM detalles_equipo d WHERE d.capacidad_disco IS NOT NULL ORDER BY d.capacidad_disco")
	List<String> findAllDiscos();

	@Query("SELECT DISTINCT d.capacidad_ram FROM detalles_equipo d WHERE d.capacidad_ram IS NOT NULL ORDER BY d.capacidad_ram")
	List<String> findAllRams();

	@Query("SELECT DISTINCT d.procesador FROM detalles_equipo d WHERE d.procesador IS NOT NULL ORDER BY d.procesador")
	List<String> findAllProcesadores();

	@Query("SELECT DISTINCT d.os FROM detalles_equipo d WHERE d.os IS NOT NULL ORDER BY d.os")
	List<String> findAllOS();
}
