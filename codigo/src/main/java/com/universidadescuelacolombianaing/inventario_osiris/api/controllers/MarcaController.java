package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.MarcaRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.MarcaResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IMarcaService;
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
@RequestMapping(path = "/mark")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MarcaController {
	private final InvalidateUserRol invalidateUserRol;
	private IMarcaService marcaService;

	@GetMapping
	public ResponseEntity<Set<MarcaResponse>> getMarks(@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.marcaService.readAll());
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<MarcaResponse> getMark(@RequestParam(name = "email", required = true) String emailParam, @PathVariable Integer id) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.marcaService.read(id));
	}

	@PostMapping
	public ResponseEntity<MarcaResponse> postMark(@Valid @RequestBody MarcaRequest request, @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.marcaService.create(request));
	}

	@PutMapping(path = "{id}")
	public ResponseEntity<MarcaResponse> putMark(@Valid @RequestBody MarcaRequest request, @PathVariable Integer id, @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 1);

		return ResponseEntity.ok(this.marcaService.update(request, id));
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> deleteMark(@PathVariable Integer id,
																												@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		this.marcaService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
