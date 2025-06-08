package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.EquipoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.ReportEquiposRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.SearchEquipoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.EquipoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioDetallesUbicacionVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioDetallesVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioSinDetallesVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IEquipoService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.InvalidateUserRol;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/device")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class EquipoController {
	private final InvalidateUserRol invalidateUserRol;
	private final IEquipoService equipoService;

	@GetMapping
	public ResponseEntity<Set<InventarioSinDetallesVwResponse>> getDevices(@RequestParam(name = "email") String emailParam,
																																				 @RequestParam(name = "field", required = false) String field,
																																				 @RequestParam(name = "value", required = false) String value) {
		invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.equipoService.readAll(field, value));
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<InventarioDetallesVwResponse> getDevice(@RequestParam(name = "email") String emailParam,
																																@PathVariable Integer id) {
		invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.equipoService.read(id));
	}

	@PostMapping
	public ResponseEntity<EquipoResponse> postDevice(@Valid @RequestBody EquipoRequest request,
																									 @RequestParam(name = "email") String emailParam) {
		String emailUser = invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.equipoService.create(request, emailUser));
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<EquipoResponse> putDevice(@Valid @RequestBody EquipoRequest request,
																									@PathVariable Integer id,
																									@RequestParam(name = "email") String emailParam) {
		String emailUser = invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.equipoService.update(request, id, emailUser));
	}

	@PostMapping("/search")
	public ResponseEntity<Set<InventarioSinDetallesVwResponse>> searchDevices(@Valid @RequestBody SearchEquipoRequest request, @RequestParam(name = "email") String emailParam) {
		invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.equipoService.search(request));
	}

	@PostMapping("/report")
	public ResponseEntity<Set<InventarioDetallesUbicacionVwResponse>> reportDevices(@Valid @RequestBody ReportEquiposRequest request, @RequestParam(name = "email") String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.equipoService.report(request));
	}

}
