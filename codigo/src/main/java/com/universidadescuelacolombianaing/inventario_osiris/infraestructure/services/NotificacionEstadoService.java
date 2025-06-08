package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.NotificacionEstadoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.EstadoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.NotificacionEstadoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.PanelNotificacionesResponse;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.EstadoEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.NotificacionEstadoEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.EstadoRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.NotificacionEstadoRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.INotificacionEstadoService;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.DuplicatedRowException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class NotificacionEstadoService implements INotificacionEstadoService {
	private final NotificacionEstadoRepository notificacionEstadoRepository;
	private final EstadoRepository estadoRepository;

	@Override
	public NotificacionEstadoResponse create(NotificacionEstadoRequest request) {
		EstadoEntity estado = this.estadoRepository.findById(request.getId_estado()).orElseThrow(() -> new IdNotFoundException("estado"));

		Set<NotificacionEstadoEntity> otherNotificacionDuplicated = this.notificacionEstadoRepository.findAllByEmailAndEstado(request.getEmail(), estado);
		if (!otherNotificacionDuplicated.isEmpty()) throw new DuplicatedRowException("email y estado");

		try {
			NotificacionEstadoEntity notificacionToPersist = NotificacionEstadoEntity.builder()
				.email(request.getEmail().toLowerCase().trim())
				.estado(estado)
				.build();

			NotificacionEstadoEntity notificacionPersisted = this.notificacionEstadoRepository.save(notificacionToPersist);

			return this.entityToResponse(notificacionPersisted);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public PanelNotificacionesResponse readAll() {
		try {
			Set<NotificacionEstadoEntity> notificaciones = new HashSet<>(this.notificacionEstadoRepository.findAll());
			Set<EstadoEntity> estados = new HashSet<>(this.estadoRepository.findAll());
			return this.entityToPanelResponse(notificaciones, estados);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public NotificacionEstadoResponse read(Integer id) {
		NotificacionEstadoEntity notificacion = this.notificacionEstadoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("notificacion estado"));
		try {
			return this.entityToResponse(notificacion);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public void delete(Integer id) {
		NotificacionEstadoEntity notificacion = this.notificacionEstadoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("notificacion estado"));
		try {
			this.notificacionEstadoRepository.delete(notificacion);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	private NotificacionEstadoResponse entityToResponse(NotificacionEstadoEntity notificacionEstadoEntity) {
		NotificacionEstadoResponse response = new NotificacionEstadoResponse();
		BeanUtils.copyProperties(notificacionEstadoEntity, response);
		response.setId_estado(notificacionEstadoEntity.getEstado().getId());
		response.setEstado(notificacionEstadoEntity.getEstado().getDescripcion());

		return response;
	}

	private PanelNotificacionesResponse entityToPanelResponse(Set<NotificacionEstadoEntity> notificaciones, Set<EstadoEntity> estados) {
		PanelNotificacionesResponse response = new PanelNotificacionesResponse();
		Set<NotificacionEstadoResponse> notificacionesResponse = new HashSet<>();
		Set<EstadoResponse> estadosResponse = new HashSet<>();

		for (NotificacionEstadoEntity notificacion : notificaciones) {
			NotificacionEstadoResponse notificacionEstadoResponse = new NotificacionEstadoResponse();
			BeanUtils.copyProperties(notificacion, notificacionEstadoResponse);
			notificacionEstadoResponse.setId_estado(notificacion.getEstado().getId());
			notificacionEstadoResponse.setEstado(notificacion.getEstado().getDescripcion());

			notificacionesResponse.add(notificacionEstadoResponse);
		}

		for (EstadoEntity estado : estados) {
			EstadoResponse estadoResponse = new EstadoResponse();
			BeanUtils.copyProperties(estado, estadoResponse);

			estadosResponse.add(estadoResponse);
		}

		response.setNotificaciones(notificacionesResponse.stream().sorted(Comparator.comparing(NotificacionEstadoResponse::getEmail)).collect(Collectors.toCollection(LinkedHashSet::new)));
		response.setEstados(estadosResponse.stream().sorted(Comparator.comparing(EstadoResponse::getDescripcion)).collect(Collectors.toCollection(LinkedHashSet::new)));

		return response;
	}
}
