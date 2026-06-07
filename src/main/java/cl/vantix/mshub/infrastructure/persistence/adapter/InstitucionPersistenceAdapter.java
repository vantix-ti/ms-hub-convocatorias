package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.Institucion;
import cl.vantix.mshub.domain.port.out.InstitucionPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.InstitucionJpa;
import cl.vantix.mshub.infrastructure.persistence.repository.InstitucionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class InstitucionPersistenceAdapter implements InstitucionPersistencePort {

    private final InstitucionJpaRepository repo;

    @Override public Institucion save(Institucion inst) {
        return toDomain(repo.save(toJpa(inst)));
    }

    @Override public Optional<Institucion> findById(Long id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override public Optional<Institucion> findByNombre(String nombre) {
        return repo.findByNombre(nombre).map(this::toDomain);
    }

    @Override public List<Institucion> findAll() {
        return repo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override public boolean existsByRut(String rut) {
        return repo.existsByRut(rut);
    }

    private InstitucionJpa toJpa(Institucion inst) {
        return InstitucionJpa.builder()
                .id(inst.getId()).nombre(inst.getNombre()).rut(inst.getRut())
                .direccion(inst.getDireccion()).telefono(inst.getTelefono())
                .email(inst.getEmail()).logoUrl(inst.getLogoUrl())
                .activo(inst.isActivo()).slug(inst.getSlug()).build();
    }

    private Institucion toDomain(InstitucionJpa jpa) {
        return Institucion.builder()
                .id(jpa.getId()).nombre(jpa.getNombre()).rut(jpa.getRut())
                .direccion(jpa.getDireccion()).telefono(jpa.getTelefono())
                .email(jpa.getEmail()).logoUrl(jpa.getLogoUrl())
                .activo(jpa.isActivo()).slug(jpa.getSlug())
                .creadoEn(jpa.getCreadoEn()).actualizadoEn(jpa.getActualizadoEn())
                .build();
    }
    @Override
    public java.util.Optional<Institucion> findBySlug(String slug) {
        return repo.findBySlug(slug).map(this::toDomain);
    }
}
