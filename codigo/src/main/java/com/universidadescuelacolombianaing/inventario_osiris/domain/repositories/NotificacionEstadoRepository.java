package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.EstadoEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.NotificacionEstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NotificacionEstadoRepository extends JpaRepository<NotificacionEstadoEntity, Integer> {
	Set<NotificacionEstadoEntity> findAllByEmailAndEstado(String email, EstadoEntity estado);
	Set<NotificacionEstadoEntity> findAllByEstado(EstadoEntity estado);
}
