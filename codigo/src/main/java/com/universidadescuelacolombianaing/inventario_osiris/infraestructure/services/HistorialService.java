package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialRestoreRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.HistorialResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.MarcaResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioHistorialVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioHistorialVwEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views.InventarioHistorialVwRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IHistorialService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.EmailHelper;
import com.universidadescuelacolombianaing.inventario_osiris.utils.InventoryUtil;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.BasicBadRequestException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.MessageBadRequestException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class HistorialService implements IHistorialService {
	private final EquipoRepository equipoRepository;
	private final EstadoRepository estadoRepository;
	private final ResponsableRepository responsableRepository;
	private final BloqueRepository bloqueRepository;
	private final PisoRepository pisoRepository;
	private final UbicacionRepository ubicacionRepository;
	private final HistorialRepository historialRepository;
	private final NotificacionEstadoRepository notificacionEstadoRepository;
	private final InventarioHistorialVwRepository inventarioHistorialVwRepository;
	private final EmailHelper emailHelper;

	@Override
	public HistorialResponse create(HistorialRequest request, Integer idDevice, String emailAuthor) {
		EquipoEntity equipo = this.equipoRepository.findById(idDevice).orElseThrow(() -> new IdNotFoundException("equipo"));
		EstadoEntity estado = this.estadoRepository.findById(request.getId_estado()).orElseThrow(() -> new IdNotFoundException("estado"));
		ResponsableEntity responsable = this.responsableRepository.findById(request.getId_responsable()).orElseThrow(() -> new IdNotFoundException("responsable"));

		BloqueEntity bloque = null;
		PisoEntity piso = null;
		UbicacionEntity ubicacion = null;

		// VALIDATE FIELDS TO UPDATE LOCATION
		if (request.isChange_location()) {
			if (InventoryUtil.emptyInteger(request.getId_ubicacion())) {
				throw new MessageBadRequestException("El ID de la ubicación es obligatorio");
			}
			ubicacion = this.ubicacionRepository.findById(request.getId_ubicacion()).orElseThrow(() -> new IdNotFoundException("ubicacion"));

			if (!InventoryUtil.emptyInteger(request.getId_bloque())) {
				bloque = this.bloqueRepository.findById(request.getId_bloque()).orElseThrow(() -> new IdNotFoundException("bloque"));
			} else {
				bloque = this.bloqueRepository.findByDescripcion("N/A").orElseThrow(() -> new IdNotFoundException("bloque"));
			}

			if (!InventoryUtil.emptyInteger(request.getId_piso())) {
				piso = this.pisoRepository.findById(request.getId_piso()).orElseThrow(() -> new IdNotFoundException("piso"));
			} else {
				piso = this.pisoRepository.findByDescripcion("N/A").orElseThrow(() -> new IdNotFoundException("piso"));
			}
		}

		String today = LocalDate.now().toString();
		// UPDATE EQUIPO
		equipo.setFecha_actualizacion(today);
		equipo.setObservaciones(InventoryUtil.requiredString(request.getObservaciones(), "NO REGISTRA"));
		if (estado.getActualizar_responsable() == 1) {
			equipo.setEstado(estado);
			equipo.setResponsable(responsable);
		}

		try {
			EquipoEntity equipoUpdated = this.equipoRepository.save(equipo);

			// UPDATE LAST RECORD
			HistorialEntity lastRecordFound = this.historialRepository.findAllByEquipoByOrderIdValid(equipoUpdated).stream().toList().get(0);
			if (lastRecordFound.getFecha_fin() == null) {
				lastRecordFound.setFecha_fin(today);
				this.historialRepository.save(lastRecordFound);
			}
			// CREATE HISTORIAL
			HistorialEntity newRecordToPersist = HistorialEntity.builder()
					.equipo(equipoUpdated)
					.bloque(request.isChange_location() ? bloque : lastRecordFound.getBloque())
					.piso(request.isChange_location() ? piso : lastRecordFound.getPiso())
					.ubicacion(request.isChange_location() ? ubicacion : lastRecordFound.getUbicacion())
					.estado(estado)
					.responsable(responsable)
					.fecha_inicio(InventoryUtil.requiredString(request.getFecha_inicio(), today))
					.fecha_fin(request.getFecha_fin())
					.ticket(request.getTicket())
					.punto_red(request.getPunto_red())
					.observaciones(InventoryUtil.requiredString(request.getObservaciones(), "NO REGISTRA"))
					.actualizacion_datos(0)
					.email_autor(emailAuthor).build();

			HistorialEntity newRecordPersisted = this.historialRepository.save(newRecordToPersist);

			// SEND EMAILS
			Set<NotificacionEstadoEntity> notifications = this.notificacionEstadoRepository.findAllByEstado(estado);
			this.sendEmail(emailAuthor, estado, equipo, responsable, newRecordPersisted, notifications);

			return this.entityToResponse(newRecordPersisted);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new ServerErrorException();
		}
	}

	@Override
	public HistorialResponse restore(HistorialRestoreRequest request, Integer idDevice, String emailAuthor) {
		EquipoEntity equipo = this.equipoRepository.findById(idDevice).orElseThrow(() -> new IdNotFoundException("equipo"));

		// VALIDATE REQUEST
		HistorialEntity historial = this.historialRepository.findAllByEquipoOrderById(equipo).stream().toList().get(0);
		if (historial.getResponsable().equals(equipo.getResponsable()) && historial.getEstado().equals(equipo.getEstado())) {
			throw new BasicBadRequestException();
		}

		try {
			Set<HistorialEntity> historialValid = this.historialRepository.findAllByEquipoByOrderIdValid(equipo);
			HistorialEntity recordToPersist = new HistorialEntity();
			String today = LocalDate.now().toString();

			for (HistorialEntity record : historialValid) {
				if (record.getResponsable().equals(equipo.getResponsable())) {
					recordToPersist.setEquipo(equipo);
					recordToPersist.setEstado(record.getEstado());
					recordToPersist.setBloque(record.getBloque());
					recordToPersist.setPiso(record.getPiso());
					recordToPersist.setUbicacion(record.getUbicacion());
					recordToPersist.setResponsable(record.getResponsable());
					recordToPersist.setFecha_inicio(today);
					recordToPersist.setFecha_fin(record.getFecha_fin() != null ? record.getFecha_fin() : null);
					recordToPersist.setPunto_red(record.getPunto_red() != null ? record.getPunto_red() : null);
					recordToPersist.setObservaciones(InventoryUtil.requiredString(request.getObservaciones(), "NO REGISTRA"));
					recordToPersist.setActualizacion_datos(0);
					recordToPersist.setEmail_autor(emailAuthor);

					break;
				}
			}

			// VALIDATE SEARCH LAST RECORD VALID
			if (recordToPersist.getEquipo() == null) throw new ServerErrorException();

			equipo.setFecha_actualizacion(today);
			equipo.setObservaciones(InventoryUtil.requiredString(request.getObservaciones(), "NO REGISTRA"));
			this.equipoRepository.save(equipo);

			return this.entityToResponse(this.historialRepository.save(recordToPersist));
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new ServerErrorException();
		}
	}

	@Override
	public Set<InventarioHistorialVwResponse> read(Integer idDevice) {
		EquipoEntity equipo = this.equipoRepository.findById(idDevice).orElseThrow(() -> new IdNotFoundException("equipo"));

		try {
			Set<InventarioHistorialVwEntity> historicoFound = this.inventarioHistorialVwRepository.selectByIdEquipo(equipo.getId());

			return historicoFound.stream().map(this::entityToCompleteResponse).sorted(Comparator.comparing(InventarioHistorialVwResponse::getId).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	private void sendEmail(String emailAuthor, EstadoEntity estado, EquipoEntity equipo, ResponsableEntity responsable, HistorialEntity record, Set<NotificacionEstadoEntity> notifications) {
		Integer idState = estado.getId();

		String subject = String.format("ACTUALIZACION DEL ESTADO DEL EQUIPO #%s", equipo.getId());

		String infoDevice = String.format("""
						    <p class="normal">Se ha actualizado el estado de un equipo: <strong>%s.</strong></p>
						    <p class="subtitle">Detalles del equipo</strong>
						    <p class="normal"><strong>Tipo: </strong>%s</p>
						    <p class="normal"><strong>Modelo: </strong>%s %s</p>
						    <p class="normal"><strong>Serial: </strong>%s</p>
						    <p class="normal"><strong>Placa: </strong>%s</p>
						""", estado.getDescripcion(), equipo.getModelo().getTipo().getDescripcion(), equipo.getMarca().getDescripcion(),
				equipo.getModelo().getDescripcion(), equipo.getSerial(), equipo.getPlaca());

		String infoResponsible = String.format("""
				    <p class="normal"><strong>Nombre: </strong>%s</p>
				    <p class="normal"><strong>Email: </strong>%s</p>
				    <p class="normal"><strong>Área: </strong>%s</p>
				    <p class="normal"><strong>Emplid: </strong>%s</p>
				""", responsable.getNombre(), responsable.getEmail(), responsable.getArea(), responsable.getEmplid());

		String optionalText = "";

		// ESTADO ASIGNADO PUESTO
		if (idState == 2) optionalText = String.format("""
				   <p class="subtitle">Responsable asignado</p>
				   %s
				   <p class="subtitle">CASO ARANDA: %s</p>
				""", infoResponsible, InventoryUtil.requiredString(record.getTicket(), "NO REGISTRA"));

		// ESTADO PRESTAMO USUARIO FINAL
		if (idState == 10) optionalText = String.format("""
				    <p class="subtitle">Responsable</p>
				    %s
				    <p class="subtitle">CASO ARANDA: %s</p>
				    <p class="normal">Recuerde que el equipo en prestamo debe devolverse el mismo día.</p>
				""", infoResponsible, InventoryUtil.requiredString(record.getTicket(), "NO REGISTRA"));

		// ESTADO PRESTAMO MESA DE SERVICIO
		if (idState == 11) optionalText = String.format("""
				    <p class="subtitle">Recibido por</p>
				    %s
				    <p class="subtitle">CASO ARANDA: %s</p>
				""", infoResponsible, InventoryUtil.requiredString(record.getTicket(), "NO REGISTRA"));

		// ESTADO MANTENIMIENTO
		if (idState == 4) optionalText = "<p class='normal'>El equipo se encuentra en un mantenimiento preventivo.</p>";

		// ESTADO ALISTAMIENTO
		if (idState == 8) optionalText = "<p class='normal'>El equipo se encuentra en alistamiento técnico.</p>";

		// ESTADO DEVOLUCION NOVEDAD
		if (idState == 9) optionalText = String.format("""
						<p class="subtitle">Motivo de devolución</p>
				    <p class="normal">%s</p>
				""", record.getObservaciones());

		// ESTADO BAJA
		if (idState == 12) optionalText = String.format("""
				    <p class="subtitle">Motivo de baja</p>
				    <p class="normal">%s</p>
				""", record.getObservaciones());

		// NO SEND EMAIL IF NOT CONFIGURED EMAIL
		if (InventoryUtil.emptyString(optionalText)) return;

		String bodyEmail = String.format("""
				%s
				<br />
				%s
				""", infoDevice, optionalText);

		// SEND TO AUTHOR AND RESPONSIBLE WITHOUT DUPLICATED
		Set<String> emailsTarget = new HashSet<>();

		for (NotificacionEstadoEntity notification : notifications) {
			emailsTarget.add(notification.getEmail());
		}

		emailsTarget.add(emailAuthor);
		emailsTarget.add(responsable.getEmail());

		try {
			for (String email : emailsTarget) {
				this.emailHelper.sendEmail(email, subject, bodyEmail);
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	private HistorialResponse entityToResponse(HistorialEntity historialEntity) {
		HistorialResponse response = new HistorialResponse();
		BeanUtils.copyProperties(historialEntity, response);

		response.setId_equipo(historialEntity.getEquipo().getId());
		response.setId_estado(historialEntity.getEstado().getId());
		response.setId_bloque(historialEntity.getBloque().getId());
		response.setId_piso(historialEntity.getPiso().getId());
		response.setId_ubicacion(historialEntity.getUbicacion().getId());
		response.setId_responsable(historialEntity.getResponsable().getId());

		return response;
	}

	private InventarioHistorialVwResponse entityToCompleteResponse(InventarioHistorialVwEntity historialEntity) {
		InventarioHistorialVwResponse response = new InventarioHistorialVwResponse();
		BeanUtils.copyProperties(historialEntity, response);

		return response;
	}
}
