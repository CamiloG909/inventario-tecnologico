package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.CreateUsuarioRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.UpdateUsuarioRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.UsuarioResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IUsuarioService;
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
@RequestMapping(path = "/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class UsuarioController {
	private final InvalidateUserRol invalidateUserRol;
	private final IUsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<Set<UsuarioResponse>> getUsers(@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.usuarioService.readAll());
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<UsuarioResponse> getUser(@RequestParam(name = "email", required = true) String emailParam, @PathVariable Integer id) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.usuarioService.read(id));
	}

	@GetMapping(path = "/email/{email}")
	public ResponseEntity<UsuarioResponse> getUserByEmail(@PathVariable String email) {
		return ResponseEntity.ok(this.usuarioService.readByEmail(email));
	}

	@PostMapping
	public ResponseEntity<UsuarioResponse> postUser(@Valid @RequestBody CreateUsuarioRequest request, @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.usuarioService.create(request));
	}

	@PatchMapping(path = "{id}")
	public ResponseEntity<UsuarioResponse> changeRolUser(@Valid @RequestBody UpdateUsuarioRequest request, @PathVariable Integer id, @RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		return ResponseEntity.ok(this.usuarioService.update(request, id));
	}

	@DeleteMapping(path = "{id}")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id,
																										@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 2);

		this.usuarioService.delete(id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Deleted successfully"));
	}
}
