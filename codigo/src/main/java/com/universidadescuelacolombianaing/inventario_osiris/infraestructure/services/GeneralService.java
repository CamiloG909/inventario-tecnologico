package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.*;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IGeneralService;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class GeneralService implements IGeneralService {
	private final TipoRepository tipoRepository;
	private final ModeloRepository modeloRepository;
	private final MarcaRepository marcaRepository;
	private final BloqueRepository bloqueRepository;
	private final PisoRepository pisoRepository;
	private final UbicacionRepository ubicacionRepository;
	private final ResponsableRepository responsableRepository;
	private final EstadoRepository estadoRepository;
	private final DetallesRepository detallesRepository;

	@Override
	public FiltersResponse readFilters() {
		try {
			List<TipoEntity> tipos = this.tipoRepository.findAll();
			List<ModeloEntity> modelos = this.modeloRepository.findAll();
			List<MarcaEntity> marcas = this.marcaRepository.findAll();
			List<BloqueEntity> bloques = this.bloqueRepository.findAll();
			List<PisoEntity> pisos = this.pisoRepository.findAll();
			List<String> tipos_ubicacion = this.ubicacionRepository.findAllTiposUbicacion();
			List<UbicacionEntity> ubicaciones = this.ubicacionRepository.findAll();
			List<ResponsableEntity> responsables = this.responsableRepository.findAll();
			List<EstadoEntity> estados = this.estadoRepository.findAll();
			List<String> discos = this.detallesRepository.findAllDiscos();
			List<String> rams = this.detallesRepository.findAllRams();
			List<String> procesadores = this.detallesRepository.findAllProcesadores();
			List<String> os = this.detallesRepository.findAllOS();

			return this.entityToResponse(
				tipos,
				modelos,
				marcas,
				bloques,
				pisos,
				tipos_ubicacion,
				ubicaciones,
				responsables,
				estados,
				discos,
				rams,
				procesadores,
				os
			);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new ServerErrorException();
		}
	}

	private FiltersResponse entityToResponse(List<TipoEntity> tipos,
																					 List<ModeloEntity> modelos,
																					 List<MarcaEntity> marcas,
																					 List<BloqueEntity> bloques,
																					 List<PisoEntity> pisos,
																					 List<String> tipos_ubicacion,
																					 List<UbicacionEntity> ubicaciones,
																					 List<ResponsableEntity> responsables,
																					 List<EstadoEntity> estados,
																					 List<String> discos,
																					 List<String> rams,
																					 List<String> procesadores,
																					 List<String> os) {
		FiltersResponse response = new FiltersResponse();

		Set<TipoResponse> tiposResponse = new HashSet<>();
		Set<ModeloResponse> modelosResponse = new HashSet<>();
		Set<MarcaResponse> marcasResponse = new HashSet<>();
		Set<BloqueResponse> bloquesResponse = new HashSet<>();
		Set<PisoResponse> pisosResponse = new HashSet<>();
		Set<String> tiposUbicacionResponse = new HashSet<>(tipos_ubicacion);
		Set<UbicacionResponse> ubicacionesResponse = new HashSet<>();
		Set<ResponsableResponse> responsablesResponse = new HashSet<>();
		Set<EstadoResponse> estadosResponse = new HashSet<>();
		Set<String> discosResponse = new HashSet<>(discos);
		Set<String> ramsResponse = new HashSet<>(rams);
		Set<String> procesadoresResponse = new HashSet<>(procesadores);
		Set<String> osResponse = new HashSet<>(os);

		for (TipoEntity tipo : tipos) {
			TipoResponse tipoResponse = new TipoResponse();
			BeanUtils.copyProperties(tipo, tipoResponse);
			tiposResponse.add(tipoResponse);
		}

		for (ModeloEntity modelo : modelos) {
			ModeloResponse modeloResponse = new ModeloResponse();
			BeanUtils.copyProperties(modelo, modeloResponse);
			modeloResponse.setId_tipo(modelo.getTipo().getId());
			modelosResponse.add(modeloResponse);
		}

		for (MarcaEntity marca : marcas) {
			MarcaResponse marcaResponse = new MarcaResponse();
			BeanUtils.copyProperties(marca, marcaResponse);
			marcasResponse.add(marcaResponse);
		}

		for (BloqueEntity bloque : bloques) {
			BloqueResponse bloqueResponse = new BloqueResponse();
			BeanUtils.copyProperties(bloque, bloqueResponse);
			bloquesResponse.add(bloqueResponse);
		}

		for (PisoEntity piso : pisos) {
			PisoResponse pisoResponse = new PisoResponse();
			BeanUtils.copyProperties(piso, pisoResponse);
			pisosResponse.add(pisoResponse);
		}

		for (UbicacionEntity ubicacion : ubicaciones) {
			UbicacionResponse ubicacionResponse = new UbicacionResponse();
			BeanUtils.copyProperties(ubicacion, ubicacionResponse);
			ubicacionesResponse.add(ubicacionResponse);
		}

		for (ResponsableEntity responsable : responsables) {
			ResponsableResponse responsableResponse = new ResponsableResponse();
			BeanUtils.copyProperties(responsable, responsableResponse);
			responsablesResponse.add(responsableResponse);
		}

		for (EstadoEntity estado : estados) {
			EstadoResponse estadoResponse = new EstadoResponse();
			BeanUtils.copyProperties(estado, estadoResponse);
			estadosResponse.add(estadoResponse);
		}

		response.setTipos(tiposResponse.stream().sorted(Comparator.comparing(TipoResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setModelos(modelosResponse.stream().sorted(Comparator.comparing(ModeloResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setMarcas(marcasResponse.stream().sorted(Comparator.comparing(MarcaResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setBloques(bloquesResponse.stream().sorted(Comparator.comparing(BloqueResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setPisos(pisosResponse.stream().sorted(Comparator.comparing(PisoResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setTipos_ubicacion(tiposUbicacionResponse.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setUbicaciones(ubicacionesResponse.stream().sorted(Comparator.comparing(UbicacionResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setResponsables(responsablesResponse.stream().sorted(Comparator.comparing(ResponsableResponse::getNombre)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setEstados(estadosResponse.stream().sorted(Comparator.comparing(EstadoResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setDiscos(discosResponse.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setRams(ramsResponse.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setProcesadores(procesadoresResponse.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setOs(osResponse.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(LinkedHashSet::new)));

		return response;
	}
}
