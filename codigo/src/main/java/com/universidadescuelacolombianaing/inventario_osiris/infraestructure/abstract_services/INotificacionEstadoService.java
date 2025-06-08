package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.NotificacionEstadoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.NotificacionEstadoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.PanelNotificacionesResponse;

public interface INotificacionEstadoService {
	NotificacionEstadoResponse create(NotificacionEstadoRequest request);

	PanelNotificacionesResponse readAll();

	NotificacionEstadoResponse read(Integer id);

	void delete(Integer id);
}
