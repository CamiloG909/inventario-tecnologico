package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.EncargadoMantenimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EncargadoMantenimientoRepository extends JpaRepository<EncargadoMantenimientoEntity, Integer> {
	List<EncargadoMantenimientoEntity> findAllByEstado(Integer estado);
}
