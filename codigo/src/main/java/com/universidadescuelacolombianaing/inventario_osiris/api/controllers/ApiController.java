package com.universidadescuelacolombianaing.inventario_osiris.api.controllers;

import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping
@Slf4j
public class ApiController {
	@GetMapping
	public ResponseEntity<Map<String, String>> handler() {
		try {
			return ResponseEntity.ok(Collections.singletonMap("message", "Welcome to API from Inventario OSIRIS ECIJG"));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}
}
