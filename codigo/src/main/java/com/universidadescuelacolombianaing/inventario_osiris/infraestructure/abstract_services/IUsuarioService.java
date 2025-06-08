package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.CreateUsuarioRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.UpdateUsuarioRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.UsuarioResponse;

import java.util.Set;

public interface IUsuarioService{
	UsuarioResponse create(CreateUsuarioRequest request);

	UsuarioResponse read(Integer id);

	UsuarioResponse readByEmail(String email);

	Set<UsuarioResponse> readAll();

	UsuarioResponse update(UpdateUsuarioRequest request, Integer id);

	void delete(Integer id);
}
