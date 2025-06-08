package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PanelNotificacionesResponse {
	private Set<NotificacionEstadoResponse> notificaciones;
	private Set<EstadoResponse> estados;
}
