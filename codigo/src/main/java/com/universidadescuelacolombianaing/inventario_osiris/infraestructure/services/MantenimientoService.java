package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialMantenimientoPreguntaRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.HistorialMantenimientoRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.RolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioRolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.*;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.RolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.UsuarioRolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IMantenimientoService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.EmailHelper;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.ServerErrorException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class MantenimientoService implements IMantenimientoService {
	private final EquipoRepository equipoRepository;
	private final TipoRepository tipoRepository;
	private final EncargadoMantenimientoRepository encargadoMantenimientoRepository;
	private final HistorialMantenimientoRepository historialMantenimientoRepository;
	private final HistorialMantenimientoPreguntaRepository historialMantenimientoPreguntaRepository;
	private final PreguntaMantenimientoRepository preguntaMantenimientoRepository;
	private final RolRepository rolRepository;
	private final UsuarioRolRepository usuarioRolRepository;
	private final EmailHelper emailHelper;

	@Override
	public Set<HistorialMantenimientoResponse> readHistoric(Integer id) {
		EquipoEntity equipo = this.equipoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("equipo"));

		try {
			return equipo.getMantenimientos().stream().map(this::historialMantenimientoToResponse).sorted(Comparator.comparing(HistorialMantenimientoResponse::getFecha).reversed()).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public HistorialMantenimientoResponse createHistoricDevice(Integer id, HistorialMantenimientoRequest request) {
		EquipoEntity equipo = this.equipoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("equipo"));
		EncargadoMantenimientoEntity encargadoMantenimiento = this.encargadoMantenimientoRepository.findById(request.getId_encargado_mantenimiento()).orElseThrow(() -> new IdNotFoundException("encargado"));

		try {
			HistorialMantenimientoEntity historial = this.historialMantenimientoRepository.save(HistorialMantenimientoEntity.builder().equipo(equipo).fecha(LocalDate.parse(request.getFecha())).novedades(request.getNovedades()).encargado(encargadoMantenimiento).build());

			Set<HistorialMantenimientoPreguntaEntity> preguntasToPersist = new HashSet<>();

			for (HistorialMantenimientoPreguntaRequest pregunta : request.getPreguntas()) {
				Optional<PreguntaMantenimientoEntity> preguntaFound = this.preguntaMantenimientoRepository.findById(pregunta.getId_pregunta());

				if (preguntaFound.isEmpty()) {
					continue;
				}

				preguntasToPersist.add(HistorialMantenimientoPreguntaEntity.builder().historial(historial).pregunta(preguntaFound.get()).realizado(pregunta.getRealizado()).observaciones(pregunta.getObservaciones()).build());
			}

			historial.setPreguntas(new HashSet<>(this.historialMantenimientoPreguntaRepository.saveAll(preguntasToPersist)));

			String infoDevice = String.format("""
					<p class="subtitle">Detalles del equipo</strong>
					<p class="normal"><strong>Tipo: </strong>%s</p>
					<p class="normal"><strong>Modelo: </strong>%s | %s</p>
					<p class="normal"><strong>Serial: </strong>%s</p>
					<p class="normal"><strong>Placa: </strong>%s</p>
					""", equipo.getModelo().getTipo().getDescripcion(), equipo.getMarca().getDescripcion(), equipo.getModelo().getDescripcion(), equipo.getSerial(), equipo.getPlaca());

			if (request.getNovedades() != null) {
				RolEntity rolAdmin = this.rolRepository.findByDescripcion("inventarioadmin");
				Set<UsuarioRolEntity> usuariosAdmin = this.usuarioRolRepository.findAllByRol(rolAdmin);

				String body = String.format("""
						%s
						<br />
						<p class="subtitle">NOVEDAD: %s</p>
						<p class="normal"><strong>Encargado: </strong>%s</p>
						""", infoDevice, request.getNovedades().trim(), encargadoMantenimiento.getNombre().toUpperCase() + " (" + encargadoMantenimiento.getEmail() + ")");

				for (UsuarioRolEntity usuario : usuariosAdmin) {
					try {
						this.emailHelper.sendEmail(usuario.getUsuario().getEmail(), String.format("NOVEDAD EN MANTENIMIENTO DEL EQUIPO #%s", equipo.getId()), body);
					} catch (RuntimeException e) {
						System.out.println(e.getMessage());
					}
				}
			}

			try {
				this.emailHelper.sendEmail(equipo.getResponsable().getEmail(), String.format("MANTENIMIENTO REALIZADO DE %s A SU CARGO", equipo.getModelo().getTipo().getDescripcion().toUpperCase()),
						String.format("""
								%s
								<br />
								<p class="subtitle">Se llevó a cabo una tarea de mantenimiento el día %s. Si detecta cualquier inconveniente, por favor, contactese con Servicios TI.</p>
								""", infoDevice, request.getFecha()));
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}

			return historialMantenimientoToResponse(historial);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public Set<PreguntaMantenimientoResponse> readQuestionsOfType(Integer id) {
		TipoEntity tipo = this.tipoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("tipo"));

		try {
			return tipo.getPreguntas().stream().map(i -> preguntaMantenimientoToResponse(i.getPregunta())).filter(i -> i.getEstado().equals(1)).sorted(Comparator.comparing(PreguntaMantenimientoResponse::getId)).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public Set<EncargadoMantenimientoResponse> readResponsibles() {
		try {
			List<EncargadoMantenimientoEntity> encargados = this.encargadoMantenimientoRepository.findAllByEstado(1);

			return encargados.stream().map(this::encargadoMantenimientoToResponse).sorted(Comparator.comparing(EncargadoMantenimientoResponse::getNombre)).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public Set<MantenimientoReporteResponse> getReport() {
		try {
			List<HistorialMantenimientoEntity> historico = this.historialMantenimientoRepository.findAll();

			Set<MantenimientoReporteResponse> response = new HashSet<>();

			for (HistorialMantenimientoEntity historial : historico) {
				EncargadoMantenimientoEntity encargado = historial.getEncargado();
				EquipoEntity equipo = historial.getEquipo();
				MarcaEntity marca = equipo.getMarca();
				ModeloEntity modelo = equipo.getModelo();
				TipoEntity tipo = modelo.getTipo();
				ResponsableEntity responsable = equipo.getResponsable();
				DetallesEntity detalles = equipo.getDetalles();
				HistorialEntity ultimoHistorial = equipo.getHistorialEquipos().stream().max(Comparator.comparingInt(HistorialEntity::getId)).orElse(null);

				for (HistorialMantenimientoPreguntaEntity historialPregunta : historial.getPreguntas()) {
					PreguntaMantenimientoEntity pregunta = historialPregunta.getPregunta();

					response.add(MantenimientoReporteResponse.builder()
							.id(historial.getId())
							.tipo(tipo.getDescripcion())
							.marca(marca.getDescripcion())
							.modelo(modelo.getDescripcion())
							.serial(equipo.getSerial())
							.placa(equipo.getPlaca())
							.numero(equipo.getNumero())
							.host(detalles != null ? detalles.getHost() : null)
							.responsable(responsable.getNombre())
							.bloque(ultimoHistorial != null ? ultimoHistorial.getBloque().getDescripcion() : null)
							.piso(ultimoHistorial != null ? ultimoHistorial.getPiso().getDescripcion() : null)
							.ubicacion(ultimoHistorial != null ? ultimoHistorial.getUbicacion().getDescripcion() : null)
							.fecha(historial.getFecha())
							.novedades_mantenimiento(historial.getNovedades())
							.encargado_mantenimiento(encargado.getNombre())
							.pregunta(pregunta.getPregunta())
							.realizado(historialPregunta.getRealizado())
							.observaciones(historialPregunta.getObservaciones())
							.build());
				}
			}

			return response.stream().sorted(Comparator.comparing(MantenimientoReporteResponse::getId)).collect(Collectors.toCollection(java.util.LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	private HistorialMantenimientoResponse historialMantenimientoToResponse(HistorialMantenimientoEntity entity) {
		HistorialMantenimientoResponse response = new HistorialMantenimientoResponse();
		BeanUtils.copyProperties(entity, response);

		response.setEncargado(encargadoMantenimientoToResponse(entity.getEncargado()));

		List<HistorialMantenimientoPreguntaResponse> preguntas = new ArrayList<>();

		for (HistorialMantenimientoPreguntaEntity pregunta : entity.getPreguntas().stream().sorted(Comparator.comparing(i -> i.getPregunta().getId())).toList()) {
			HistorialMantenimientoPreguntaResponse answerResponse = new HistorialMantenimientoPreguntaResponse();
			BeanUtils.copyProperties(pregunta, answerResponse);

			answerResponse.setPregunta(pregunta.getPregunta().getPregunta());

			preguntas.add(answerResponse);
		}

		response.setPreguntas(preguntas);

		return response;
	}

	private EncargadoMantenimientoResponse encargadoMantenimientoToResponse(EncargadoMantenimientoEntity entity) {
		EncargadoMantenimientoResponse response = new EncargadoMantenimientoResponse();
		BeanUtils.copyProperties(entity, response);

		return response;
	}

	private PreguntaMantenimientoResponse preguntaMantenimientoToResponse(PreguntaMantenimientoEntity entity) {
		PreguntaMantenimientoResponse response = new PreguntaMantenimientoResponse();
		BeanUtils.copyProperties(entity, response);

		return response;
	}

}
