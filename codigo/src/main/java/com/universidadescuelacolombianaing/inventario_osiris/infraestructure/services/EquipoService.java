package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.EquipoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.ReportEquiposRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.SearchEquipoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.EquipoResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioDetallesUbicacionVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioDetallesVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.views.InventarioSinDetallesVwResponse;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.RolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioRolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioDetallesUbicacionVwEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioDetallesVwEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioHistorialUltimoRegistroVwEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.views.InventarioSinDetallesVwEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.RolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.UsuarioRolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views.InventarioDetallesUbicacionVwRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views.InventarioDetallesVwRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views.InventarioHistorialUltimoRegistroVwRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.views.InventarioSinDetallesVwRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IEquipoService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.ConverterHelper;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.EmailHelper;
import com.universidadescuelacolombianaing.inventario_osiris.utils.InventoryUtil;
import com.universidadescuelacolombianaing.inventario_osiris.utils.enums.FieldsToSearchEquipo;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.DuplicatedRowException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.MessageBadRequestException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class EquipoService implements IEquipoService {
	private final InventarioDetallesVwRepository inventarioDetallesVwRepository;
	private final InventarioDetallesUbicacionVwRepository inventarioDetallesUbicacionVwRepository;
	private final InventarioSinDetallesVwRepository inventarioSinDetallesVwRepository;
	private final InventarioHistorialUltimoRegistroVwRepository inventarioHistorialUltimoRegistroVwRepository;
	private final DetallesRepository detallesRepository;
	private final MarcaRepository marcaRepository;
	private final ModeloRepository modeloRepository;
	private final EstadoRepository estadoRepository;
	private final ResponsableRepository responsableRepository;
	private final EquipoRepository equipoRepository;
	private final HistorialRepository historialRepository;
	private final BloqueRepository bloqueRepository;
	private final PisoRepository pisoRepository;
	private final UbicacionRepository ubicacionRepository;
	private final RolRepository rolRepository;
	private final UsuarioRolRepository usuarioRolRepository;
	private final EmailHelper emailHelper;
	private final ConverterHelper converterHelper;

	@Override
	public Set<InventarioSinDetallesVwResponse> readAll(String field, String value) {
		try {
			List<InventarioSinDetallesVwEntity> equiposFound = this.inventarioSinDetallesVwRepository.findAll();

			Set<InventarioSinDetallesVwResponse> response = new HashSet<>();

			if (field != null) {
				if (field.equals("bloque")) {
					List<Integer> equiposBloqueFound = this.inventarioHistorialUltimoRegistroVwRepository.findAllByBloqueContainingIgnoreCase(value).stream().map(InventarioHistorialUltimoRegistroVwEntity::getId_equipo).toList();
					response = equiposFound.stream().filter((equipo) -> (equiposBloqueFound.contains(equipo.getId()))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("estado")) {
					response = equiposFound.stream().filter((equipo) -> (equipo.getEstado().equalsIgnoreCase(value))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("marca")) {
					response = equiposFound.stream().filter((equipo) -> (equipo.getMarca().equalsIgnoreCase(value))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("modelo")) {
					response = equiposFound.stream().filter((equipo) -> (equipo.getModelo().equalsIgnoreCase(value))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("responsable")) {
					response = equiposFound.stream().filter((equipo) -> (equipo.getResponsable_nombre().equalsIgnoreCase(value))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("tipo")) {
					response = equiposFound.stream().filter((equipo) -> (equipo.getTipo_equipo().equalsIgnoreCase(value))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("os")) {
					List<Integer> equiposOsFound = this.inventarioDetallesVwRepository.findAllByOsContainingIgnoreCase(value).stream().map(InventarioDetallesVwEntity::getId).toList();
					response = equiposFound.stream().filter((equipo) -> (equiposOsFound.contains(equipo.getId()))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
				if (field.equals("ubicacion")) {
					List<Integer> equiposUbicacionFound = this.inventarioHistorialUltimoRegistroVwRepository.findAllByUbicacionContainingIgnoreCase(value).stream().map(InventarioHistorialUltimoRegistroVwEntity::getId_equipo).toList();
					response = equiposFound.stream().filter((equipo) -> (equiposUbicacionFound.contains(equipo.getId()))).map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
				}
			} else {
				response = equiposFound.stream().map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());
			}

			return response.stream()
					.sorted((o1, o2) -> InventoryUtil.dateVwToLocalDate(o2.getFecha_actualizacion())
							.compareTo(InventoryUtil.dateVwToLocalDate(o1.getFecha_actualizacion())))
					.collect(Collectors.toCollection(LinkedHashSet::new));
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new ServerErrorException();
		}
	}

	@Override
	public InventarioDetallesVwResponse read(Integer id) {
		InventarioDetallesVwEntity equipo = this.inventarioDetallesVwRepository.findById(id).orElseThrow(() -> new IdNotFoundException("equipo"));
		try {
			return this.entityToInventarioDetallesVwResponse(equipo);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public EquipoResponse create(EquipoRequest request, String emailAuthor) {
		if (request.getId_bloque() == null || request.getId_piso() == null || request.getId_ubicacion() == null) {
			throw new MessageBadRequestException("No se han enviado todos los campos");
		}

		Set<EquipoEntity> foundDevicesBySerial = this.equipoRepository.findAllBySerialIgnoreCase(request.getSerial());

		if(!foundDevicesBySerial.isEmpty()) throw new DuplicatedRowException("serial");

		BloqueEntity bloque = this.bloqueRepository.findById(request.getId_bloque()).orElseThrow(() -> new IdNotFoundException("bloque"));
		PisoEntity piso = this.pisoRepository.findById(request.getId_piso()).orElseThrow(() -> new IdNotFoundException("piso"));
		UbicacionEntity ubicacion = this.ubicacionRepository.findById(request.getId_ubicacion()).orElseThrow(() -> new IdNotFoundException("ubicacion"));

		MarcaEntity marca = this.marcaRepository.findById(request.getId_marca()).orElseThrow(() -> new IdNotFoundException("marca"));
		ModeloEntity modelo = this.modeloRepository.findById(request.getId_modelo()).orElseThrow(() -> new IdNotFoundException("modelo"));
		EstadoEntity estado = this.estadoRepository.findById(request.getId_estado()).orElseThrow(() -> new IdNotFoundException("estado"));
		ResponsableEntity responsable = this.responsableRepository.findById(request.getId_responsable()).orElseThrow(() -> new IdNotFoundException("responsable"));

		try {
			EquipoEntity equipoToPersist = new EquipoEntity();
			String today = LocalDate.now().toString();

			equipoToPersist.setMarca(marca);
			equipoToPersist.setModelo(modelo);
			equipoToPersist.setEstado(estado);
			equipoToPersist.setResponsable(responsable);

			equipoToPersist.setSerial(request.getSerial().trim().toUpperCase());
			equipoToPersist.setPlaca(InventoryUtil.requiredString(request.getPlaca(), "NO REGISTRA"));
			equipoToPersist.setNumero(InventoryUtil.requiredString(request.getNumero(), "NO REGISTRA"));
			equipoToPersist.setObservaciones(InventoryUtil.requiredString(request.getObservaciones(), "NO REGISTRA"));
			equipoToPersist.setDatos_adicionales(request.getDatos_adicionales());
			equipoToPersist.setFecha_actualizacion(today);

			// SAVE DETALLES
			if (request.getDatos_adicionales() == 1) {
				DetallesEntity detallesToPersist = DetallesEntity.builder()
						.host(InventoryUtil.requiredString(request.getDetalles().getHost(), "NO REGISTRA"))
						.tipo_disco(InventoryUtil.requiredString(request.getDetalles().getTipo_disco(), "NO REGISTRA"))
						.capacidad_disco(InventoryUtil.requiredString(request.getDetalles().getCapacidad_disco(), "NO REGISTRA").toUpperCase())
						.capacidad_ram(InventoryUtil.requiredString(request.getDetalles().getCapacidad_ram(), "NO REGISTRA").toUpperCase())
						.procesador(InventoryUtil.requiredString(request.getDetalles().getProcesador(), "NO REGISTRA").toUpperCase())
						.os(InventoryUtil.requiredString(request.getDetalles().getOs(), "NO REGISTRA").toUpperCase())
						.mac_address(InventoryUtil.requiredString(request.getDetalles().getMac_address(), "NO REGISTRA").toUpperCase())
						.build();

				DetallesEntity detallesPersisted = this.detallesRepository.save(detallesToPersist);
				equipoToPersist.setDetalles(detallesPersisted);
			}

			EquipoEntity equipoPersisted = this.equipoRepository.save(equipoToPersist);

			// SAVE HISTORIAL
			HistorialEntity historialToPersist = HistorialEntity.builder()
					.equipo(equipoPersisted)
					.estado(estado)
					.bloque(bloque)
					.piso(piso)
					.ubicacion(ubicacion)
					.responsable(responsable)
					.fecha_inicio(today)
					.observaciones(InventoryUtil.requiredString(request.getObservaciones(), "NO REGISTRA"))
					.actualizacion_datos(0)
					.email_autor(emailAuthor)
					.build();

			this.historialRepository.save(historialToPersist);

			// SEND EMAIL TO ADMINS
			RolEntity rolAdmin = this.rolRepository.findByDescripcion("inventarioadmin");
			Set<UsuarioRolEntity> usuariosAdmin = this.usuarioRolRepository.findAllByRol(rolAdmin);

			for (UsuarioRolEntity usuario : usuariosAdmin) {
				String usuarioEmail = usuario.getUsuario().getEmail();
				try {
					this.emailHelper.sendEmail(usuarioEmail,
							String.format("SE HA AÑADIDO UN NUEVO EQUIPO (SERIAL %s)",
									equipoPersisted.getSerial()),
							String.format("<p class='normal'>Se ha creado un nuevo equipo con ID-%s, por: %s.</p>",
									equipoPersisted.getId(), emailAuthor));
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
			}

			return this.entityToEquipoResponse(equipoPersisted);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public EquipoResponse update(EquipoRequest request, Integer id, String emailAuthor) {
		if (InventoryUtil.emptyString(request.getObservaciones())) {
			throw new MessageBadRequestException("Las observaciones son obligatorias");
		}

		Set<ResponsableEntity> responsable = this.responsableRepository.findAllByEmail(emailAuthor);
		if (responsable.size() != 1) throw new IdNotFoundException("usuario");

		EquipoEntity equipoToUpdate = this.equipoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("equipo"));
		HistorialEntity lastRecordFound = this.historialRepository.findAllByEquipoByOrderIdValid(equipoToUpdate).stream().toList().get(0);
		if (lastRecordFound == null) throw new IdNotFoundException("historico");

		MarcaEntity marca = this.marcaRepository.findById(request.getId_marca()).orElseThrow(() -> new IdNotFoundException("marca"));
		ModeloEntity modelo = this.modeloRepository.findById(request.getId_modelo()).orElseThrow(() -> new IdNotFoundException("modelo"));

		try {
			boolean deleteDetalles = request.getDatos_adicionales() == 0 && equipoToUpdate.getDatos_adicionales() == 1;
			Integer idDetallesToDelete = equipoToUpdate.getDetalles() != null ? equipoToUpdate.getDetalles().getId() : null;

			//UPDATE DETALLES
			if (request.getDatos_adicionales() == 1 && equipoToUpdate.getDatos_adicionales() == 1) {
				if (equipoToUpdate.getDetalles() != null) {
					DetallesEntity detallesToUpdate = equipoToUpdate.getDetalles();
					detallesToUpdate.setHost(InventoryUtil.requiredString(request.getDetalles().getHost(), "NO REGISTRA"));
					detallesToUpdate.setTipo_disco(InventoryUtil.requiredString(request.getDetalles().getTipo_disco(), "NO REGISTRA"));
					detallesToUpdate.setCapacidad_disco(InventoryUtil.requiredString(request.getDetalles().getCapacidad_disco(), "NO REGISTRA").toUpperCase());
					detallesToUpdate.setCapacidad_ram(InventoryUtil.requiredString(request.getDetalles().getCapacidad_ram(), "NO REGISTRA").toUpperCase());
					detallesToUpdate.setProcesador(InventoryUtil.requiredString(request.getDetalles().getProcesador(), "NO REGISTRA").toUpperCase());
					detallesToUpdate.setOs(InventoryUtil.requiredString(request.getDetalles().getOs(), "NO REGISTRA").toUpperCase());
					detallesToUpdate.setMac_address(InventoryUtil.requiredString(request.getDetalles().getMac_address(), "NO REGISTRA").toUpperCase());

					this.detallesRepository.save(detallesToUpdate);
				}
			}

			// CREATE ROW DETALLES
			if (request.getDatos_adicionales() == 1 && equipoToUpdate.getDatos_adicionales() == 0) {
				DetallesEntity detallesToPersist = DetallesEntity.builder()
						.host(InventoryUtil.requiredString(request.getDetalles().getHost(), "NO REGISTRA"))
						.tipo_disco(InventoryUtil.requiredString(request.getDetalles().getTipo_disco(), "NO REGISTRA"))
						.capacidad_disco(InventoryUtil.requiredString(request.getDetalles().getCapacidad_disco(), "NO REGISTRA").toUpperCase())
						.capacidad_ram(InventoryUtil.requiredString(request.getDetalles().getCapacidad_ram(), "NO REGISTRA").toUpperCase())
						.procesador(InventoryUtil.requiredString(request.getDetalles().getProcesador(), "NO REGISTRA").toUpperCase())
						.os(InventoryUtil.requiredString(request.getDetalles().getOs(), "NO REGISTRA").toUpperCase())
						.mac_address(InventoryUtil.requiredString(request.getDetalles().getMac_address(), "NO REGISTRA").toUpperCase())
						.build();

				DetallesEntity detallesPersisted = this.detallesRepository.save(detallesToPersist);
				equipoToUpdate.setDetalles(detallesPersisted);
			}

			// SET DETALLES NULL IN EQUIPO
			if (deleteDetalles) {
				equipoToUpdate.setDetalles(null);
			}

			String today = LocalDate.now().toString();

			equipoToUpdate.setMarca(marca);
			equipoToUpdate.setModelo(modelo);

			equipoToUpdate.setSerial(request.getSerial().trim().toUpperCase());
			equipoToUpdate.setPlaca(InventoryUtil.requiredString(request.getPlaca(), "NO REGISTRA"));
			equipoToUpdate.setNumero(InventoryUtil.requiredString(request.getNumero(), "NO REGISTRA"));
			equipoToUpdate.setObservaciones(request.getObservaciones().trim());
			equipoToUpdate.setDatos_adicionales(request.getDatos_adicionales());
			equipoToUpdate.setFecha_actualizacion(today);

			EquipoEntity equipoPersisted = this.equipoRepository.saveAndFlush(equipoToUpdate);

			// DELETE ROW DETALLES
			if (deleteDetalles && idDetallesToDelete != null) {
				this.detallesRepository.deleteById(idDetallesToDelete);
			}

			// SAVE HISTORIAL
			HistorialEntity historialToPersist = HistorialEntity.builder()
					.equipo(equipoPersisted)
					.estado(lastRecordFound.getEstado())
					.bloque(lastRecordFound.getBloque())
					.piso(lastRecordFound.getPiso())
					.ubicacion(lastRecordFound.getUbicacion())
					.responsable(responsable.stream().toList().get(0))
					.fecha_inicio(today)
					.observaciones(request.getObservaciones().trim())
					.actualizacion_datos(1)
					.email_autor(emailAuthor)
					.build();

			this.historialRepository.save(historialToPersist);

			// SEND EMAIL TO ADMINS
			RolEntity rolAdmin = this.rolRepository.findByDescripcion("inventarioadmin");
			Set<UsuarioRolEntity> usuariosAdmin = this.usuarioRolRepository.findAllByRol(rolAdmin);

			for (UsuarioRolEntity usuario : usuariosAdmin) {
				String usuarioEmail = usuario.getUsuario().getEmail();
				try {
					this.emailHelper.sendEmail(usuarioEmail,
							String.format("SE HAN ACTUALIZADO LAS CARACTERÍSTICAS DEL EQUIPO #%s",
									equipoPersisted.getId()),
							String.format("<p class='normal'>Las características del equipo con ID-%s han sido actualizadas por: %s.</p>",
									equipoPersisted.getId(), emailAuthor));
				} catch (RuntimeException e) {
					System.out.println(e.getMessage());
				}
			}

			return this.entityToEquipoResponse(equipoPersisted);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new ServerErrorException();
		}
	}

	public Set<InventarioSinDetallesVwResponse> search(SearchEquipoRequest request) {
		if (!EnumUtils.isValidEnum(FieldsToSearchEquipo.class, request.getField())) {
			throw new MessageBadRequestException("El campo de búsqueda no es válido");
		}
		String value = request.getValue().trim();

		List<InventarioSinDetallesVwEntity> equiposFound = new ArrayList<>();

		//INVENTARIO DETALLES TO INVENTARIO SIN DETALLES
		if (InventoryUtil.stringIncludes(request.getField(), List.of("host", "procesador", "os", "mac_address"))) {
			List<InventarioDetallesVwEntity> equiposDetallesFound = new ArrayList<>();

			if (request.getField().equals("host")) {
				equiposDetallesFound = this.inventarioDetallesVwRepository.findAllByHostContainingIgnoreCase(value);
			}
			if (request.getField().equals("procesador")) {
				equiposDetallesFound = this.inventarioDetallesVwRepository.findAllByProcesadorContainingIgnoreCase(value);
			}
			if (request.getField().equals("os")) {
				equiposDetallesFound = this.inventarioDetallesVwRepository.findAllByOsContainingIgnoreCase(value);
			}
			if (request.getField().equals("mac_address")) {
				equiposDetallesFound = this.inventarioDetallesVwRepository.selectByMacAddress(value);
			}

			try {
				Set<InventarioSinDetallesVwResponse> response = equiposDetallesFound.stream().map(this::entityDetallesToSinDetallesVwResponse).collect(Collectors.toSet());

				return response.stream()
						.sorted((o1, o2) -> InventoryUtil.dateVwToLocalDate(o2.getFecha_actualizacion())
								.compareTo(InventoryUtil.dateVwToLocalDate(o1.getFecha_actualizacion())))
						.collect(Collectors.toCollection(LinkedHashSet::new));
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				throw new ServerErrorException();
			}
		}

		if (request.getField().equals("id")) {
			Optional<InventarioSinDetallesVwEntity> equipoFound = this.inventarioSinDetallesVwRepository.findById(converterHelper.toInt(request.getValue()));
			equipoFound.ifPresent(equiposFound::add);
		}
		if (request.getField().equals("serial")) {
			equiposFound = this.inventarioSinDetallesVwRepository.findAllBySerialContainingIgnoreCase(value);
		}
		if (request.getField().equals("placa")) {
			equiposFound = this.inventarioSinDetallesVwRepository.findAllByPlacaContainingIgnoreCase(value);
		}
		if (request.getField().equals("numero")) {
			equiposFound = this.inventarioSinDetallesVwRepository.findAllByNumero(value);
		}
		if (request.getField().equals("marca")) {
			equiposFound = this.inventarioSinDetallesVwRepository.findAllByMarcaContainingIgnoreCase(value);
		}
		if (request.getField().equals("modelo")) {
			equiposFound = this.inventarioSinDetallesVwRepository.findAllByModeloContainingIgnoreCase(value);
		}

		try {
			Set<InventarioSinDetallesVwResponse> response = equiposFound.stream().map(this::entityToInventarioSinDetallesVwResponse).collect(Collectors.toSet());

			return response.stream()
					.sorted((o1, o2) -> InventoryUtil.dateVwToLocalDate(o2.getFecha_actualizacion())
							.compareTo(InventoryUtil.dateVwToLocalDate(o1.getFecha_actualizacion())))
					.collect(Collectors.toCollection(LinkedHashSet::new));
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			throw new ServerErrorException();
		}
	}

	public Set<InventarioDetallesUbicacionVwResponse> report(ReportEquiposRequest request) {
		Set<InventarioDetallesUbicacionVwEntity> equipos = new HashSet<>();

		if (request.getEquipos().size() <= 2000) {
			equipos = this.inventarioDetallesUbicacionVwRepository.selectByMultipleIds(request.getEquipos());
		} else {
			int groups = (int) Math.ceil((double) request.getEquipos().size() / 2000);

			int index = 1;
			int limit = groups > 1 ? 2000 : request.getEquipos().size();

			for (int i = 1; i <= groups; i++) {
				Set<Integer> ids = new HashSet<>();
				for (int j = index; j <= (groups == i ? (limit - (limit - request.getEquipos().size())) : limit); j++) {
					ids.add(request.getEquipos().stream().toList().get(j - 1));
				}

				equipos.addAll(this.inventarioDetallesUbicacionVwRepository.selectByMultipleIds(ids));
				index += 2000;
				limit += 2000;
			}
		}

		Set<InventarioDetallesUbicacionVwResponse> response = equipos.stream().map(this::entityToInventarioDetallesUbicacionVwResponse).collect(Collectors.toSet());

		return response.stream().sorted((o1, o2) -> InventoryUtil.dateVwToLocalDate(o2.getFecha_actualizacion())
						.compareTo(InventoryUtil.dateVwToLocalDate(o1.getFecha_actualizacion())))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private InventarioDetallesVwResponse entityToInventarioDetallesVwResponse(InventarioDetallesVwEntity equipoEntity) {
		InventarioDetallesVwResponse response = new InventarioDetallesVwResponse();
		BeanUtils.copyProperties(equipoEntity, response);

		return response;
	}

	private InventarioDetallesUbicacionVwResponse entityToInventarioDetallesUbicacionVwResponse(InventarioDetallesUbicacionVwEntity equipoEntity) {
		InventarioDetallesUbicacionVwResponse response = new InventarioDetallesUbicacionVwResponse();
		BeanUtils.copyProperties(equipoEntity, response);

		return response;
	}


	private InventarioSinDetallesVwResponse entityDetallesToSinDetallesVwResponse(InventarioDetallesVwEntity equipoEntity) {
		InventarioSinDetallesVwResponse response = new InventarioSinDetallesVwResponse();
		BeanUtils.copyProperties(equipoEntity, response);

		return response;
	}

	private InventarioSinDetallesVwResponse entityToInventarioSinDetallesVwResponse(InventarioSinDetallesVwEntity equipoEntity) {
		InventarioSinDetallesVwResponse response = new InventarioSinDetallesVwResponse();
		BeanUtils.copyProperties(equipoEntity, response);

		return response;
	}

	private EquipoResponse entityToEquipoResponse(EquipoEntity equipoEntity) {
		EquipoResponse response = new EquipoResponse();
		BeanUtils.copyProperties(equipoEntity, response);

		response.setId_marca(equipoEntity.getMarca().getId());
		response.setId_modelo(equipoEntity.getModelo().getId());
		response.setId_estado(equipoEntity.getEstado().getId());
		response.setId_responsable(equipoEntity.getResponsable().getId());
		response.setId_detalles_equipo(equipoEntity.getDetalles() != null ? equipoEntity.getDetalles().getId() : null);

		return response;
	}
}
