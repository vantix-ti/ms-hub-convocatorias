package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface NotificacionJpaRepository extends JpaRepository<NotificacionJpa, Long> {
    List<NotificacionJpa> findByDestinatario_IdOrderByCreadoEnDesc(Long destinatarioId);
    List<NotificacionJpa> findByDestinatario_IdAndLeidaFalse(Long destinatarioId);
}
