package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.FiltersResponse;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IGeneralService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.InvalidateUserRol;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class GeneralController {
	private final InvalidateUserRol invalidateUserRol;
	private final IGeneralService generalService;

	@GetMapping("/filters")
	public ResponseEntity<FiltersResponse> getFilters(@RequestParam(name = "email", required = true) String emailParam) {
		invalidateUserRol.invalidate(emailParam, 0);

		return ResponseEntity.ok(this.generalService.readFilters());
	}
}
