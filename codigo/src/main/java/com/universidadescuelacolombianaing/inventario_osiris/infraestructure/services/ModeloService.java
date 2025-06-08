package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.ModeloRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.ModeloResponse;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.ModeloEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.TipoEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.ModeloRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.TipoRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IModeloService;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.MessageBadRequestException;
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
public class ModeloService implements IModeloService {
	private final TipoRepository tipoRepository;
	private final ModeloRepository modeloRepository;

	@Override
	public ModeloResponse create(ModeloRequest request) {
		TipoEntity tipo = this.tipoRepository.findById(request.getId_tipo()).orElseThrow(() -> new IdNotFoundException("modelo"));
		Optional<ModeloEntity> modelo = this.modeloRepository.findByDescripcionContainingIgnoreCase(request.getDescripcion().trim());
		if (modelo.isPresent()) throw new MessageBadRequestException("El modelo ya se encuentra registrado");

		try {
			ModeloEntity modeloToPersist = ModeloEntity.builder()
				.descripcion(request.getDescripcion().toUpperCase().trim())
				.tipo(tipo)
				.build();

			ModeloEntity modeloPersisted = this.modeloRepository.save(modeloToPersist);

			return this.entityToResponse(modeloPersisted);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public ModeloResponse read(Integer id) {
		ModeloEntity modelo = this.modeloRepository.findById(id).orElseThrow(() -> new IdNotFoundException("modelo"));
		try {
			return this.entityToResponse(modelo);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public Set<ModeloResponse> readAll() {
		try {
			List<ModeloEntity> modelosFound = this.modeloRepository.findAll();
			return modelosFound.stream().map(this::entityToResponse).sorted(Comparator.comparing(ModeloResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public ModeloResponse update(ModeloRequest request, Integer id) {
		ModeloEntity modelo = this.modeloRepository.findById(id).orElseThrow(() -> new IdNotFoundException("modelo"));

		if (!modelo.getDescripcion().equalsIgnoreCase(request.getDescripcion().trim())) {
			Optional<ModeloEntity> modeloDuplicated = this.modeloRepository.findByDescripcionContainingIgnoreCase(request.getDescripcion().trim());
			if (modeloDuplicated.isPresent()) throw new MessageBadRequestException("El modelo ya se encuentra registrada");
		}

		TipoEntity tipo = this.tipoRepository.findById(request.getId_tipo()).orElseThrow(() -> new IdNotFoundException("tipo"));

		modelo.setDescripcion(request.getDescripcion().toUpperCase().trim());
		modelo.setTipo(tipo);

		try {
			ModeloEntity modeloUpdated = this.modeloRepository.save(modelo);
			return this.entityToResponse(modeloUpdated);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public void delete(Integer id) {
		ModeloEntity modelo = this.modeloRepository.findById(id).orElseThrow(() -> new IdNotFoundException("modelo"));
		try {
			this.modeloRepository.delete(modelo);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	private ModeloResponse entityToResponse(ModeloEntity modeloEntity) {
		ModeloResponse response = new ModeloResponse();
		BeanUtils.copyProperties(modeloEntity, response);

		response.setId_tipo(modeloEntity.getTipo().getId());

		return response;
	}
}
