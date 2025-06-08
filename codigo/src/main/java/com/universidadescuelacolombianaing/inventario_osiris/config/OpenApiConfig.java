package com.universidadescuelacolombianaing.inventario_osiris.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "Inventario Osiris API",
		version = "2.0",
		description = "Documentation of Inventario Osiris API for endpoints"
	)
)
public class OpenApiConfig {

}
