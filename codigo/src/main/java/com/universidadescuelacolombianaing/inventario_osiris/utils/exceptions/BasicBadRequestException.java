package com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions;

public class BasicBadRequestException extends RuntimeException {
	public BasicBadRequestException() {
		super("Solicitud no válida");
	}
}
