package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PostulacionJpaRepository extends JpaRepository<PostulacionJpa, Long> {
    List<PostulacionJpa> findByPostulante_Id(Long postulanteId);
    List<PostulacionJpa> findByConvocatoria_Id(Long convocatoriaId);
    long countByConvocatoria_Id(Long convocatoriaId);
}
