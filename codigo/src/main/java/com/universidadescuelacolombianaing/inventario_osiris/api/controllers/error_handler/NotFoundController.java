package com.universidadescuelacolombianaing.inventario_osiris.api.controllers.error_handler;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.BaseErrorResponse;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundController {

	@ExceptionHandler({
		IdNotFoundException.class,
	})
	public BaseErrorResponse handleNotFound(IdNotFoundException exception) {
		System.out.println(exception.getMessage());
		return BaseErrorResponse.builder()
			.message(exception.getMessage())
			.code(HttpStatus.NOT_FOUND.value())
			.build()
			;
	}
}
