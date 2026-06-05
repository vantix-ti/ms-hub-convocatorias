package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.Convocatoria;
import cl.vantix.mshub.domain.port.out.ConvocatoriaPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.mapper.ConvocatoriaPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConvocatoriaPersistenceAdapter implements ConvocatoriaPersistencePort {

    private final ConvocatoriaJpaRepository repo;
    private final EstadoConvocatoriaJpaRepository estadoRepo;
    private final TipoRegistroJpaRepository tipoRegistroRepo;
    private final EtiquetaJpaRepository etiquetaRepo;
    private final UsuarioJpaRepository usuarioRepo;
    private final ConvocatoriaPersistenceMapper mapper;

    @Override public List<Convocatoria> findPublicas() {
        return repo.findByEstado_Nombre("PUBLICADA").stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public List<Convocatoria> findAll() {
        return repo.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public Optional<Convocatoria> findById(Long id) {
        return repo.findById(id).map(mapper::toDomain);
    }
    @Override public boolean existsById(Long id) { return repo.existsById(id); }
    @Override public void deleteById(Long id) { repo.deleteById(id); }

    @Override public Convocatoria save(Convocatoria domain) {
        EstadoConvocatoriaJpa estado = estadoRepo.findByNombre(domain.getEstado().name())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + domain.getEstado().name()));
        TipoRegistroJpa tipoReg = tipoRegistroRepo.findByNombre(domain.getTipoRegistro().name())
                .orElseThrow(() -> new RuntimeException("TipoRegistro no encontrado"));
        Set<EtiquetaJpa> etiquetas = new HashSet<>();
        if (domain.getEtiquetas() != null) {
            domain.getEtiquetas().forEach(e ->
                etiquetas.add(etiquetaRepo.findByNombre(e.getNombre())
                        .orElseGet(() -> etiquetaRepo.save(EtiquetaJpa.builder()
                                .nombre(e.getNombre()).color(e.getColor()).build()))));
        }
        UsuarioJpa creadoPor = domain.getCreadoPorId() != null
                ? usuarioRepo.findById(domain.getCreadoPorId()).orElse(null) : null;
        ConvocatoriaJpa jpa = repo.findById(domain.getId() != null ? domain.getId() : -1L)
                .orElse(ConvocatoriaJpa.builder().build());
        jpa.setTitulo(domain.getTitulo());
        jpa.setDescripcion(domain.getDescripcion());
        jpa.setOrganizacion(domain.getOrganizacion());
        jpa.setFechaInicio(domain.getFechaInicio());
        jpa.setFechaFin(domain.getFechaFin());
        jpa.setImagen(domain.getImagen());
        jpa.setEstado(estado);
        jpa.setTipoRegistro(tipoReg);
        jpa.setMaxPostulacionesPorUsuario(domain.getMaxPostulacionesPorUsuario());
        jpa.setCreadoPor(creadoPor);
        jpa.setEtiquetas(etiquetas);
        return mapper.toDomain(repo.save(jpa));
    }
}
