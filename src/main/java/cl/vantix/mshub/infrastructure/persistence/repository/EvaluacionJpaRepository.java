package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface EvaluacionJpaRepository extends JpaRepository<EvaluacionJpa, Long> {
    List<EvaluacionJpa> findByRevisor_Id(Long revisorId);
    List<EvaluacionJpa> findByPostulacion_Id(Long postulacionId);
    List<EvaluacionJpa> findByEtapa_Id(Long etapaId);
    boolean existsByPostulacion_IdAndRevisor_IdAndEtapa_Id(Long postulacionId, Long revisorId, Long etapaId);
}
