package cl.vantix.mshub.domain.port.out;
import cl.vantix.mshub.domain.model.Notificacion;
import java.util.List;
import java.util.Optional;

public interface NotificacionPersistencePort {
    List<Notificacion> findByDestinatarioIdOrderByCreadoEnDesc(Long destinatarioId);
    List<Notificacion> findByDestinatarioIdAndLeidaFalse(Long destinatarioId);
    Optional<Notificacion> findById(Long id);
    Notificacion save(Notificacion notificacion);
    void saveAll(List<Notificacion> notificaciones);
}
