package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.NotificacionEstadoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.NotificacionEstadoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.PanelNotificacionesResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.INotificacionEstadoService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.InvalidateUserRol;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path = "/notification")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class NotificacionEstadoController {
	private final InvalidateUserRol invalidateUserRol;
	private final INotificacionEstadoService notificacionEstadoService;

	@GetMapping
	public ResponseEntity<PanelNotificacionesResponse> getNotifications(@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.notificacionEstadoService.readAll());
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<NotificacionEstadoResponse> getNotification(@RequestParam(name = "email", required = true) String emailParam,
																																		@PathVariable Integer id) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.notificacionEstadoService.read(id));
	}

	@PostMapping
	public ResponseEntity<NotificacionEstadoResponse> postNotification(@Valid @RequestBody NotificacionEstadoRequest request,
																																		 @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.notificacionEstadoService.create(request));
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable Integer id,
																																@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		this.notificacionEstadoService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
