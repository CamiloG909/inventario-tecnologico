package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialRestoreRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.HistorialResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioHistorialVwResponse;

import java.util.Set;

public interface IHistorialService {
	HistorialResponse create(HistorialRequest request, Integer idDevice, String emailAuthor);

	HistorialResponse restore(HistorialRestoreRequest request, Integer idDevice, String emailAuthor);

	Set<InventarioHistorialVwResponse> read(Integer idDevice);
}
