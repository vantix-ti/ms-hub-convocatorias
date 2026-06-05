package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Notificacion;
import java.util.List;

public interface NotificacionUseCase {
    List<Notificacion> listarPorUsuario(Long usuarioId);
    void marcarLeida(Long notificacionId, Long usuarioId);
    void marcarTodasLeidas(Long usuarioId);
    Notificacion crear(Long destinatarioId, String titulo, String mensaje,
                       cl.vantix.mshub.domain.model.TipoNotificacion tipo);
}
