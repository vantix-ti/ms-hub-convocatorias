package cl.vantix.mshub.infrastructure.persistence.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class NotificacionPersistenceMapper {

    public Notificacion toDomain(NotificacionJpa jpa) {
        if (jpa == null) return null;
        return Notificacion.builder()
                .id(jpa.getId())
                .destinatarioId(jpa.getDestinatario().getId())
                .tipo(TipoNotificacion.valueOf(jpa.getTipoNotificacion().getNombre()))
                .titulo(jpa.getTitulo()).mensaje(jpa.getMensaje())
                .leida(jpa.isLeida()).creadoEn(jpa.getCreadoEn())
                .build();
    }
}
