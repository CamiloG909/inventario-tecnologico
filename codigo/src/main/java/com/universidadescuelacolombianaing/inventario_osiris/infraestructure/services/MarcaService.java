package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.MarcaRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.MarcaResponse;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.MarcaEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.MarcaRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IMarcaService;
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
public class MarcaService implements IMarcaService {
	private final MarcaRepository marcaRepository;

	@Override
	public MarcaResponse create(MarcaRequest request) {
		Optional<MarcaEntity> marca = this.marcaRepository.findByDescripcionContainingIgnoreCase(request.getDescripcion().trim());
		if (marca.isPresent()) throw new MessageBadRequestException("La marca ya se encuentra registrada");

		try {
			MarcaEntity marcaToPersist = MarcaEntity.builder()
				.descripcion(request.getDescripcion().toUpperCase().trim())
				.build();

			MarcaEntity marcaPersisted = this.marcaRepository.save(marcaToPersist);

			return this.entityToResponse(marcaPersisted);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public MarcaResponse read(Integer id) {
		MarcaEntity marca = this.marcaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("marca"));
		try {
			return this.entityToResponse(marca);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public Set<MarcaResponse> readAll() {
		try {
			List<MarcaEntity> marcasFound = this.marcaRepository.findAll();
			return marcasFound.stream().map(this::entityToResponse).sorted(Comparator.comparing(MarcaResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public MarcaResponse update(MarcaRequest request, Integer id) {
		MarcaEntity marca = this.marcaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("marca"));

		if(!marca.getDescripcion().equalsIgnoreCase(request.getDescripcion().trim())) {
			Optional<MarcaEntity> marcaDuplicated = this.marcaRepository.findByDescripcionContainingIgnoreCase(request.getDescripcion().trim());
			if (marcaDuplicated.isPresent()) throw new MessageBadRequestException("La marca ya se encuentra registrada");
		}

		marca.setDescripcion(request.getDescripcion().toUpperCase().trim());

		try {
			MarcaEntity marcaUpdated = this.marcaRepository.save(marca);
			return this.entityToResponse(marcaUpdated);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public void delete(Integer id) {
		MarcaEntity marca = this.marcaRepository.findById(id).orElseThrow(() -> new IdNotFoundException("marca"));
		try {
			this.marcaRepository.delete(marca);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	private MarcaResponse entityToResponse(MarcaEntity marcaEntity) {
		MarcaResponse response = new MarcaResponse();
		BeanUtils.copyProperties(marcaEntity, response);

		return response;
	}
}
