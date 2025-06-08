package com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions;

public class NoPermissionsException extends RuntimeException {
	public NoPermissionsException() {
		super("No tiene los permisos necesarios");
	}
}
