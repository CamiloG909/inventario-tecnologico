package com.universidadescuelacolombianaing.inventario_osiris.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

public class InventoryUtil {
	public static String decryptEmail(String email) {
		byte[] bytesDecodificados = Base64.getDecoder().decode(email);
		return new String(bytesDecodificados);
	}

	public static boolean emptyInteger(Integer value) {
		if (value == null) return true;
		return value == 0;
	}

	public static boolean emptyString(String text) {
		if (text == null) return true;
		return text.isEmpty();
	}

	public static String requiredString(String text, String replacementText) {
		if (text == null) return replacementText;
		if (text.isEmpty()) return replacementText;
		return text.trim();
	}

	public static LocalDate dateVwToLocalDate(String dateText) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(dateText, formatter);
	}

	public static boolean stringIncludes(String text, List<String> equalValues) {
		return equalValues.contains(text.trim());
	}
}
