package cl.vantix.mshub.domain.port.out;

import cl.vantix.mshub.domain.model.Institucion;
import java.util.List;
import java.util.Optional;

public interface InstitucionPersistencePort {
    Institucion save(Institucion institucion);
    Optional<Institucion> findById(Long id);
    Optional<Institucion> findByNombre(String nombre);
    List<Institucion> findAll();
    boolean existsByRut(String rut);
}
