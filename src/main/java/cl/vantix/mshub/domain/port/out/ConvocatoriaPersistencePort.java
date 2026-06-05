package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.Convocatoria;
import cl.vantix.mshub.domain.model.EstadoConvocatoria;
import java.util.List;
import java.util.Optional;

public interface ConvocatoriaPersistencePort {
    List<Convocatoria> findPublicas();
    List<Convocatoria> findAll();
    Optional<Convocatoria> findById(Long id);
    Convocatoria save(Convocatoria convocatoria);
    void deleteById(Long id);
    boolean existsById(Long id);
}
