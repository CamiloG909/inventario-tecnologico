package com.universidadescuelacolombianaing.inventario_osiris.domain.repositories;

import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.ResponsableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ResponsableRepository extends JpaRepository<ResponsableEntity, Integer> {
	Set<ResponsableEntity> findAllByEmail(String email);

	@Query("SELECT t FROM responsable t WHERE t.estado = 1 ORDER BY t.nombre")
	List<ResponsableEntity> findAllActive();
}
