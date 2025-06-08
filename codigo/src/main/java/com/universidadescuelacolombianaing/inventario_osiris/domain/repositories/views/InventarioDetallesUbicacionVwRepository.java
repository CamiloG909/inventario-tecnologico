package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioDetallesUbicacionVwEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Set;

public interface InventarioDetallesUbicacionVwRepository extends JpaRepository<InventarioDetallesUbicacionVwEntity, Integer> {
	@Query("SELECT t FROM inventario_con_detalles_ubicacion t WHERE t.id IN :ids")
	Set<InventarioDetallesUbicacionVwEntity> selectByMultipleIds(Collection<Integer> ids);
}
