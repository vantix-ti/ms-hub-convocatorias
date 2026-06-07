package cl.vantix.mshub.infrastructure.persistence.repository;

import cl.vantix.mshub.infrastructure.persistence.entity.InstitucionJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstitucionJpaRepository extends JpaRepository<InstitucionJpa, Long> {
    Optional<InstitucionJpa> findByNombre(String nombre);
    boolean existsByRut(String rut);
}
