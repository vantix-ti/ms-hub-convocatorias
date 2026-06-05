package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.Evaluacion;
import java.util.List;
import java.util.Optional;

public interface EvaluacionPersistencePort {
    Optional<Evaluacion> findById(Long id);
    List<Evaluacion> findByRevisorId(Long revisorId);
    List<Evaluacion> findByPostulacionId(Long postulacionId);
    List<Evaluacion> findByEtapaId(Long etapaId);
    Evaluacion save(Evaluacion evaluacion);
    boolean existsByPostulacionIdAndRevisorIdAndEtapaId(Long postulacionId, Long revisorId, Long etapaId);
}
