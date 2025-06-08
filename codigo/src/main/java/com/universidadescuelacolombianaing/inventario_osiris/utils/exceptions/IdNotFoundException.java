package com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions;

public class IdNotFoundException extends RuntimeException{
	private static final String ERROR_MESSAGE = "ID de %s no encontrado";

	public IdNotFoundException(String tableName) {
		super(String.format(ERROR_MESSAGE, tableName));
	}
}
