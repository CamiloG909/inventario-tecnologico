package com.universidadescuelacolombianaing.inventario_osiris.api.controllers.error_handler;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.BaseErrorResponse;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.NoPermissionsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedController {

	@ExceptionHandler(NoPermissionsException.class)
	public BaseErrorResponse handleUnauthorizedError(RuntimeException exception) {
		System.out.println(exception.getMessage());
		return BaseErrorResponse.builder()
			.message(exception.getMessage())
			.code(HttpStatus.UNAUTHORIZED.value())
			.build()
			;
	}
}

