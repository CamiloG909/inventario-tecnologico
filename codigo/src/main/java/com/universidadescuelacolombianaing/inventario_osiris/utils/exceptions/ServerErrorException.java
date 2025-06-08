package com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions;

public class ServerErrorException extends RuntimeException {
	public ServerErrorException() {
		super("Error del servidor");
	}
}
