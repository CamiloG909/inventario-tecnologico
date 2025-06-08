package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialMantenimientoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.EncargadoMantenimientoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.HistorialMantenimientoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.MantenimientoReporteResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.PreguntaMantenimientoResponse;

import java.util.Set;

public interface IMantenimientoService {
	Set<HistorialMantenimientoResponse> readHistoric(Integer id);

	HistorialMantenimientoResponse createHistoricDevice(Integer id, HistorialMantenimientoRequest request);

	Set<PreguntaMantenimientoResponse> readQuestionsOfType(Integer id);

	Set<EncargadoMantenimientoResponse> readResponsibles();

	Set<MantenimientoReporteResponse> getReport();
}
