package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers;

import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.MessageBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConverterHelper {
	public Integer toInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (RuntimeException e) {
			throw new MessageBadRequestException("El valor debe ser númerico");
		}
	}
}
