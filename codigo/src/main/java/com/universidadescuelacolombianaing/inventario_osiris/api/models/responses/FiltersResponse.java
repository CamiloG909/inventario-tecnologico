package com.universidadescuelacolombianaing.inventario_osiris.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FiltersResponse {
	private Set<TipoResponse> tipos;
	private Set<ModeloResponse> modelos;
	private Set<MarcaResponse> marcas;
	private Set<BloqueResponse> bloques;
	private Set<PisoResponse> pisos;
	private Set<String> tipos_ubicacion;
	private Set<UbicacionResponse> ubicaciones;
	private Set<ResponsableResponse> responsables;
	private Set<EstadoResponse> estados;
	private Set<String> discos;
	private Set<String> rams;
	private Set<String> procesadores;
	private Set<String> os;
}
