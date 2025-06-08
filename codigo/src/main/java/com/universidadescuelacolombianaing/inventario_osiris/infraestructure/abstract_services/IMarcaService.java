package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.MarcaRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.MarcaResponse;

import java.util.Set;

public interface IMarcaService extends CrudService<MarcaRequest, MarcaResponse, Integer> {
}
