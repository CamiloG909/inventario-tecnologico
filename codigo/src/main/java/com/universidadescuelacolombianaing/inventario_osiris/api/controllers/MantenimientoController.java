package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialMantenimientoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.EncargadoMantenimientoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.HistorialMantenimientoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.MantenimientoReporteResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.PreguntaMantenimientoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IMantenimientoService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.InvalidateUserRol;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/maintenance")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MantenimientoController {
	private final InvalidateUserRol invalidateUserRol;
	private final IMantenimientoService mantenimientoService;

	@GetMapping({"/device/{id}"})
	public ResponseEntity<Set<HistorialMantenimientoResponse>> getHistoricDevice(@RequestParam(name = "email") String emailParam, @PathVariable Integer id) {
		this.invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.mantenimientoService.readHistoric(id));
	}

	@PostMapping({"/device/{id}"})
	public ResponseEntity<HistorialMantenimientoResponse> createHistoricDevice(@Valid @RequestBody HistorialMantenimientoRequest request, @RequestParam(name = "email") String emailParam, @PathVariable Integer id) {
		this.invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.mantenimientoService.createHistoricDevice(id, request));
	}

	@GetMapping({"/type/{id}"})
	public ResponseEntity<Set<PreguntaMantenimientoResponse>> getQuestionsType(@RequestParam(name = "email") String emailParam, @PathVariable Integer id) {
		this.invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.mantenimientoService.readQuestionsOfType(id));
	}

	@GetMapping({"/responsibles"})
	public ResponseEntity<Set<EncargadoMantenimientoResponse>> getResponsibles(@RequestParam(name = "email") String emailParam) {
		this.invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.mantenimientoService.readResponsibles());
	}

	@GetMapping({"/report"})
	public ResponseEntity<Set<MantenimientoReporteResponse>> getReport(@RequestParam(name = "email") String emailParam) {
		this.invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.mantenimientoService.getReport());
	}
}
