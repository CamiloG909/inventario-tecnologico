package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.ModeloRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.ModeloResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IModeloService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.InvalidateUserRol;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "/model")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ModeloController {
	private final InvalidateUserRol invalidateUserRol;
	private IModeloService modeloService;

	@GetMapping
	public ResponseEntity<Set<ModeloResponse>> getModels(@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.modeloService.readAll());
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<ModeloResponse> getModel(@RequestParam(name = "email", required = true) String emailParam, @PathVariable Integer id) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.modeloService.read(id));
	}

	@PostMapping
	public ResponseEntity<ModeloResponse> postModel(@Valid @RequestBody ModeloRequest request, @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.modeloService.create(request));
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<ModeloResponse> putModel(@Valid @RequestBody ModeloRequest request, @PathVariable Integer id, @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.modeloService.update(request, id));
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> deleteModel(@PathVariable Integer id,
																												 @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		this.modeloService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
