package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.EquipoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.ReportEquiposRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.SearchEquipoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.EquipoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioDetallesUbicacionVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioDetallesVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioSinDetallesVwResponse;

import java.util.Set;

public interface IEquipoService {
	Set<InventarioSinDetallesVwResponse> readAll(String field, String value);

	InventarioDetallesVwResponse read(Integer id);

	EquipoResponse create(EquipoRequest request, String emailAuthor);

	EquipoResponse update(EquipoRequest request, Integer id, String emailAuthor);

	Set<InventarioSinDetallesVwResponse> search(SearchEquipoRequest request);

	Set<InventarioDetallesUbicacionVwResponse> report(ReportEquiposRequest request);
}
