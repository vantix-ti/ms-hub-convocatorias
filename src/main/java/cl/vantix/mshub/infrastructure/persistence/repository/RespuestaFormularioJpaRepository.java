package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface RespuestaFormularioJpaRepository extends JpaRepository<RespuestaFormularioJpa, Long> {
    Optional<RespuestaFormularioJpa> findByPostulacion_IdAndCampo_Id(Long postulacionId, Long campoId);
    List<RespuestaFormularioJpa> findByPostulacion_Id(Long postulacionId);
}
