package com.universidadescuelacolombianaing.inventario_osiris.infraestructure.services;

import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.CreateUsuarioRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.requests.UpdateUsuarioRequest;
import com.universidadescuelacolombianaing.inventario_osiris.api.models.responses.UsuarioResponse;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.ResponsableEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.RolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.entities.users.UsuarioRolEntity;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.ResponsableRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.RolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.UsuarioRepository;
import com.universidadescuelacolombianaing.inventario_osiris.domain.repositories.users.UsuarioRolRepository;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.abstract_services.IUsuarioService;
import com.universidadescuelacolombianaing.inventario_osiris.infraestructure.helpers.EmailHelper;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.DuplicatedRowException;
import com.universidadescuelacolombianaing.inventario_osiris.utils.exceptions.IdNotFoundException;
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
public class UsuarioService implements IUsuarioService {
	private final UsuarioRepository usuarioRepository;
	private final ResponsableRepository responsableRepository;
	private final RolRepository rolRepository;
	private final UsuarioRolRepository usuarioRolRepository;
	private final EmailHelper emailHelper;

	public Set<UsuarioResponse> readAll() {
		try {
			List<UsuarioEntity> usuariosFound = this.usuarioRepository.findAll();
			return usuariosFound.stream().map(this::entityToResponse).sorted(Comparator.comparing(UsuarioResponse::getEmail)).collect(Collectors.toCollection(LinkedHashSet::new));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public UsuarioResponse read(Integer id) {
		UsuarioEntity usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new IdNotFoundException("usuario"));
		try {
			return this.entityToResponse(usuario);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public UsuarioResponse readByEmail(String email) {
		Set<UsuarioEntity> usuario = this.usuarioRepository.findAllByEmail(email);

		if (usuario.isEmpty()) throw new IdNotFoundException("usuario");

		try {
			return this.entityToResponse(usuario.stream().toList().get(0));
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public UsuarioResponse create(CreateUsuarioRequest request) {
		Set<UsuarioEntity> otherUsuarioDuplicated = this.usuarioRepository.findAllByEmail(request.getEmail());
		if (!otherUsuarioDuplicated.isEmpty()) throw new DuplicatedRowException("email");

		RolEntity rol = this.rolRepository.findById(request.getId_rol()).orElseThrow(() -> new IdNotFoundException("rol"));

		try {
			UsuarioEntity usuarioToPersist = UsuarioEntity.builder()
					.email(request.getEmail().toLowerCase().trim())
					.build();

			UsuarioEntity usuarioPersisted = this.usuarioRepository.save(usuarioToPersist);

			UsuarioRolEntity usuarioRolToPersist = UsuarioRolEntity.builder()
					.usuario(usuarioPersisted)
					.rol(rol)
					.build();

			this.usuarioRolRepository.save(usuarioRolToPersist);

			// SI ES CONTRATISTA TOCA CREAR EL REGISTRO EN RESPONSABLE EN CASO DE QUE NO EXISTA
			if (request.isContratista()) {
				if (this.responsableRepository.findAllByEmail(request.getEmail()).isEmpty()) {
					this.responsableRepository.save(ResponsableEntity.builder()
							.emplid(Long.toString(System.currentTimeMillis()))
							.nombre(request.getEmail().split("@")[0].toUpperCase() + " CONTRATISTA")
							.email(request.getEmail())
							.area("Dirección de Tecnología Osiris")
							.estado(1)
							.rol("Contratista")
							.build());
				}
			}

			// SEND EMAIL
			try {
				this.emailHelper.sendEmail(request.getEmail(),
					"AHORA TIENE PERMISOS PARA INGRESAR",
					"<p class='normal'>Se le han asignado permisos en el Inventario Osiris, por lo que ya puede ingresar haciendo clic en el siguiente botón:</p>");
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}

			return this.entityToResponse(usuarioPersisted);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public UsuarioResponse update(UpdateUsuarioRequest request, Integer id) {
		RolEntity rol = this.rolRepository.findById(request.getId_rol()).orElseThrow(() -> new IdNotFoundException("rol"));
		UsuarioEntity usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new IdNotFoundException("usuario"));

		try {
			Set<UsuarioRolEntity> usuarioRolEntities = this.usuarioRolRepository.findAllByUsuario(usuario);

			if (usuarioRolEntities.size() != 1) throw new ServerErrorException();

			UsuarioRolEntity rolUser = usuarioRolEntities.stream().toList().get(0);
			rolUser.setRol(rol);
			this.usuarioRolRepository.save(rolUser);

			return this.entityToResponse(usuario);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public void delete(Integer id) {
		UsuarioEntity usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new IdNotFoundException("usuario"));
		try {
			this.usuarioRepository.delete(usuario);
		} catch (RuntimeException e) {
			throw new ServerErrorException();
		}
	}

	private UsuarioResponse entityToResponse(UsuarioEntity usuarioEntity) {
		UsuarioResponse response = new UsuarioResponse();
		BeanUtils.copyProperties(usuarioEntity, response);

		if (Objects.nonNull(usuarioEntity.getUsuariosRol())) {
			UsuarioRolEntity usuarioRol = usuarioEntity.getUsuariosRol().stream().toList().get(0);
			response.setId_rol(usuarioRol.getRol().getId());
			response.setRol(usuarioRol.getRol().getDescripcion());
		}

		return response;
	}
}
