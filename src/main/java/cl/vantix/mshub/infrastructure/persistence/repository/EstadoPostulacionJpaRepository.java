package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface EstadoPostulacionJpaRepository extends JpaRepository<EstadoPostulacionJpa, Long> {
    Optional<EstadoPostulacionJpa> findByNombre(String nombre);
}
