package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.CriterioEvaluacion;
import java.util.Optional;

public interface CriterioEvaluacionPersistencePort {
    Optional<CriterioEvaluacion> findById(Long id);
    CriterioEvaluacion save(CriterioEvaluacion criterio);
    void deleteById(Long id);
}
