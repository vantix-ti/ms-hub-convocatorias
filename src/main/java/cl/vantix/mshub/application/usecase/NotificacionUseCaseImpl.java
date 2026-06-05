package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.exception.UnauthorizedException;
import cl.vantix.mshub.domain.model.Notificacion;
import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.model.TipoNotificacion;
import cl.vantix.mshub.domain.model.Usuario;
import cl.vantix.mshub.domain.port.in.NotificacionUseCase;
import cl.vantix.mshub.domain.port.out.NotificacionPersistencePort;
import cl.vantix.mshub.domain.port.out.UsuarioPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionUseCaseImpl implements NotificacionUseCase {
    private final NotificacionPersistencePort notifPort;
    private final UsuarioPersistencePort usuarioPort;

    @Override
    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notifPort.findByDestinatarioIdOrderByCreadoEnDesc(usuarioId);
    }
    @Override @Transactional
    public void marcarLeida(Long notificacionId, Long usuarioId) {
        Notificacion n = notifPort.findById(notificacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada."));
        if (!n.getDestinatarioId().equals(usuarioId))
            throw new UnauthorizedException("Sin permisos.");
        n.setLeida(true);
        notifPort.save(n);
    }
    @Override @Transactional
    public void marcarTodasLeidas(Long usuarioId) {
        List<Notificacion> pendientes = notifPort.findByDestinatarioIdAndLeidaFalse(usuarioId);
        pendientes.forEach(n -> n.setLeida(true));
        notifPort.saveAll(pendientes);
    }
    @Override @Transactional
    public Notificacion crear(Long destinatarioId, String titulo, String mensaje, TipoNotificacion tipo) {
        return notifPort.save(Notificacion.builder()
                .destinatarioId(destinatarioId).titulo(titulo).mensaje(mensaje)
                .tipo(tipo).leida(false).build());
    }

    @Override
    public int enviarMasiva(String destinatarios, String titulo, String mensaje) {
        List<Usuario> destinatariosLista;
        if ("TODOS".equalsIgnoreCase(destinatarios)) {
            destinatariosLista = usuarioPort.findAll();
        } else {
            try {
                destinatariosLista = usuarioPort.findByRol(Rol.valueOf(destinatarios.toUpperCase()));
            } catch (IllegalArgumentException e) {
                destinatariosLista = usuarioPort.findAll();
            }
        }
        for (Usuario u : destinatariosLista) {
            crear(u.getId(), titulo, mensaje, TipoNotificacion.SISTEMA);
        }
        return destinatariosLista.size();
    }
}