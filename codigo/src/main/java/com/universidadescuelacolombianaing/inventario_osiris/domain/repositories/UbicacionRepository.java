package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.UbicacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UbicacionRepository extends JpaRepository<UbicacionEntity, Integer> {
	@Query("SELECT DISTINCT t.categoria FROM ubicacion t WHERE t.categoria IS NOT NULL ORDER BY t.categoria")
	List<String> findAllTiposUbicacion();
}
