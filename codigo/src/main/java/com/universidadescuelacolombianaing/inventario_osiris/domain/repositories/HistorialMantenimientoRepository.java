package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.HistorialMantenimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialMantenimientoRepository extends JpaRepository<HistorialMantenimientoEntity, Integer> {
}
