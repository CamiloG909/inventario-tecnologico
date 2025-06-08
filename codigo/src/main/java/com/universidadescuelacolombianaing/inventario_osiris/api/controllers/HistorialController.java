package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialRestoreRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.HistorialResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioHistorialVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IHistorialService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.InvalidateUserRol;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/device-history")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class HistorialController {
	private final InvalidateUserRol invalidateUserRol;
	private final IHistorialService historialService;

	@GetMapping(path = "{idDevice}")
	public ResponseEntity<Set<InventarioHistorialVwResponse>> getHistory(@RequestParam(name = "email", required = true) String emailParam,
																																			 @PathVariable Integer idDevice) {
		invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.historialService.read(idDevice));
	}

	@Operation(summary = "Cambiar el estado de un equipo")
	@PostMapping(path = "{idDevice}")
	public ResponseEntity<HistorialResponse> postHistory(@Valid @RequestBody HistorialRequest request,
																											 @RequestParam(name = "email", required = true) String emailParam,
																											 @PathVariable Integer idDevice) {
		String emailAuthor = invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.historialService.create(request, idDevice, emailAuthor));
	}

	@Operation(summary = "Retornar el equipo al responsable asignado")
	@PutMapping(path = "{idDevice}/restore")
	public ResponseEntity<HistorialResponse> putHistory(@Valid @RequestBody HistorialRestoreRequest request,
																											@RequestParam(name = "email", required = true) String emailParam,
																											@PathVariable Integer idDevice
	) {
		String emailAuthor = invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.historialService.restore(request, idDevice, emailAuthor));
	}
}
