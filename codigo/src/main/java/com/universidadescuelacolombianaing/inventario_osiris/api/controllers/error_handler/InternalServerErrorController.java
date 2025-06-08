package com.universidadescuelacolombianaing.inventario_osiris.api.controllers.error_handler;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.BaseErrorResponse;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorController {

	@ExceptionHandler({ServerErrorException.class, DataIntegrityViolationException.class})
	public BaseErrorResponse handleInternalServerError(RuntimeException exception) {
		System.out.println(exception.getMessage());
		return BaseErrorResponse.builder()
			.message("Ha ocurrido un error")
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.build()
			;
	}
}
