package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioDetallesVwEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioDetallesVwRepository extends JpaRepository<InventarioDetallesVwEntity, Integer> {
	List<InventarioDetallesVwEntity> findAllByHostContainingIgnoreCase(String host);
	List<InventarioDetallesVwEntity> findAllByProcesadorContainingIgnoreCase(String procesador);
	List<InventarioDetallesVwEntity> findAllByOsContainingIgnoreCase(String os);
	@Query("SELECT t FROM inventario_con_detalles t WHERE t.mac_address = ?1")
	List<InventarioDetallesVwEntity> selectByMacAddress(String macAddress);
}
