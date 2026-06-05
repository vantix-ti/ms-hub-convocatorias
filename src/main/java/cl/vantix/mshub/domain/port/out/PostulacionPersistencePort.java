package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.Postulacion;
import java.util.List;
import java.util.Optional;

public interface PostulacionPersistencePort {
    Optional<Postulacion> findById(Long id);
    List<Postulacion> findByPostulanteId(Long postulanteId);
    List<Postulacion> findByConvocatoriaId(Long convocatoriaId);
    List<Postulacion> findAll();
    Postulacion save(Postulacion postulacion);
    long countByConvocatoriaId(Long convocatoriaId);
}
