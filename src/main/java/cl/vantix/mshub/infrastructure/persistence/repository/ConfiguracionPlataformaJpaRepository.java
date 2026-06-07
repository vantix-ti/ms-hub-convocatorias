package cl.vantix.mshub.infrastructure.persistence.repository;

import cl.vantix.mshub.infrastructure.persistence.entity.ConfiguracionPlataformaJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConfiguracionPlataformaJpaRepository extends JpaRepository<ConfiguracionPlataformaJpa, Long> {
    List<ConfiguracionPlataformaJpa> findByInstitucionId(Long institucionId);
    Optional<ConfiguracionPlataformaJpa> findByInstitucionIdAndClave(Long institucionId, String clave);
}
