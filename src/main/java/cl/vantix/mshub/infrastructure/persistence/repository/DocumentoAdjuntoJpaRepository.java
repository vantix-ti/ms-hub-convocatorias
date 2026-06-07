package cl.vantix.mshub.infrastructure.persistence.repository;

import cl.vantix.mshub.infrastructure.persistence.entity.DocumentoAdjuntoJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentoAdjuntoJpaRepository extends JpaRepository<DocumentoAdjuntoJpa, Long> {
    List<DocumentoAdjuntoJpa> findByConvocatoriaId(Long convocatoriaId);
    void deleteByConvocatoriaId(Long convocatoriaId);
}
