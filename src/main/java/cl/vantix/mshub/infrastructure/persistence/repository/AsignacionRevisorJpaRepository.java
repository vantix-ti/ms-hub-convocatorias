package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface AsignacionRevisorJpaRepository extends JpaRepository<AsignacionRevisorJpa, Long> {
    List<AsignacionRevisorJpa> findByPostulacion_Id(Long postulacionId);
    boolean existsByPostulacion_IdAndRevisor_IdAndEtapa_Id(Long postulacionId, Long revisorId, Long etapaId);
}
