package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioSinDetallesVwEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioSinDetallesVwRepository extends JpaRepository<InventarioSinDetallesVwEntity, Integer> {
	List<InventarioSinDetallesVwEntity> findAllBySerialContainingIgnoreCase(String serial);

	List<InventarioSinDetallesVwEntity> findAllByPlacaContainingIgnoreCase(String placa);

	List<InventarioSinDetallesVwEntity> findAllByNumero(String numero);

	List<InventarioSinDetallesVwEntity> findAllByMarcaContainingIgnoreCase(String marca);

	List<InventarioSinDetallesVwEntity> findAllByModeloContainingIgnoreCase(String modelo);
}
