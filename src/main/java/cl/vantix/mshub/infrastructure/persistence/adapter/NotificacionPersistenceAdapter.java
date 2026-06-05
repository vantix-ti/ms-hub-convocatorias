package cl.vantix.mshub.infrastructure.persistence.adapter;

import cl.vantix.mshub.domain.model.Notificacion;
import cl.vantix.mshub.domain.port.out.NotificacionPersistencePort;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import cl.vantix.mshub.infrastructure.persistence.mapper.NotificacionPersistenceMapper;
import cl.vantix.mshub.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class NotificacionPersistenceAdapter implements NotificacionPersistencePort {

    private final NotificacionJpaRepository notifRepo;
    private final UsuarioJpaRepository usuarioRepo;
    private final TipoNotificacionJpaRepository tipoRepo;
    private final NotificacionPersistenceMapper mapper;

    @Override public List<Notificacion> findByDestinatarioIdOrderByCreadoEnDesc(Long id) {
        return notifRepo.findByDestinatario_IdOrderByCreadoEnDesc(id)
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public List<Notificacion> findByDestinatarioIdAndLeidaFalse(Long id) {
        return notifRepo.findByDestinatario_IdAndLeidaFalse(id)
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override public Optional<Notificacion> findById(Long id) {
        return notifRepo.findById(id).map(mapper::toDomain);
    }
    @Override public Notificacion save(Notificacion domain) {
        UsuarioJpa destinatario = usuarioRepo.findById(domain.getDestinatarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TipoNotificacionJpa tipo = tipoRepo.findByNombre(domain.getTipo().name())
                .orElseThrow(() -> new RuntimeException("TipoNotificacion no encontrado"));
        NotificacionJpa jpa = domain.getId() != null
                ? notifRepo.findById(domain.getId()).orElse(NotificacionJpa.builder().build())
                : NotificacionJpa.builder().build();
        jpa.setDestinatario(destinatario); jpa.setTipoNotificacion(tipo);
        jpa.setTitulo(domain.getTitulo()); jpa.setMensaje(domain.getMensaje());
        jpa.setLeida(domain.isLeida());
        return mapper.toDomain(notifRepo.save(jpa));
    }
    @Override public void saveAll(List<Notificacion> notificaciones) {
        notificaciones.forEach(this::save);
    }
}
