package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import java.util.Set;

public interface CrudService<RQ, RS, ID> {
	RS create(RQ request);

	RS read(ID id);

	Set<RS> readAll();

	RS update(RQ request, ID id);

	void delete(ID id);
}
