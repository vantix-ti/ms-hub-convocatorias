package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.model.Usuario;
import cl.vantix.mshub.domain.port.out.UsuarioPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.RolJpa;
import cl.vantix.mshub.infrastructure.persistence.entity.UsuarioJpa;
import cl.vantix.mshub.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.RolJpaRepository;
import cl.vantix.mshub.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioPersistencePort {

    private final UsuarioJpaRepository usuarioRepo;
    private final RolJpaRepository rolRepo;
    private final UsuarioPersistenceMapper mapper;

    @Override public Optional<Usuario> findByEmail(String email) {
        return usuarioRepo.findByEmail(email).map(mapper::toDomain);
    }
    @Override public Optional<Usuario> findById(Long id) {
        return usuarioRepo.findById(id).map(mapper::toDomain);
    }
    @Override public Optional<Usuario> findByTokenConfirmacion(String token) {
        return usuarioRepo.findByTokenConfirmacion(token).map(mapper::toDomain);
    }
    @Override public Optional<Usuario> findByTokenReset(String token) {
        return usuarioRepo.findByTokenReset(token).map(mapper::toDomain);
    }
    @Override public List<Usuario> findByRol(Rol rol) {
        return usuarioRepo.findByRoles_Nombre(rol.name())
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public List<Usuario> findAll() {
        return usuarioRepo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public boolean existsByEmail(String email) {
        return usuarioRepo.existsByEmail(email);
    }
    @Override public Usuario save(Usuario usuario) {
        Set<RolJpa> rolesJpa = usuario.getRoles() == null ? new HashSet<>() :
                usuario.getRoles().stream()
                        .map(r -> rolRepo.findByNombre(r.name())
                                .orElseGet(() -> rolRepo.save(RolJpa.builder().nombre(r.name()).build())))
                        .collect(Collectors.toSet());
        UsuarioJpa jpa = mapper.toJpa(usuario, rolesJpa);
        return mapper.toDomain(usuarioRepo.save(jpa));
    }

    @Override
    public List<Usuario> findByInstitucionId(Long institucionId) {
        return usuarioRepo.findByInstitucionId(institucionId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
}
