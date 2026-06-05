package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.AsignacionRevisor;
import java.util.List;

public interface AsignacionRevisorPersistencePort {
    AsignacionRevisor save(AsignacionRevisor asignacion);
    List<AsignacionRevisor> findByPostulacionId(Long postulacionId);
    boolean existsByPostulacionIdAndRevisorIdAndEtapaId(Long postulacionId, Long revisorId, Long etapaId);
}
