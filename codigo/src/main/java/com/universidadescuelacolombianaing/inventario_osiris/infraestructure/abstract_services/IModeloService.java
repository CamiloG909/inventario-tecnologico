package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.ModeloRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.ModeloResponse;

import java.util.Set;

public interface IModeloService extends CrudService<ModeloRequest, ModeloResponse, Integer> {
}
