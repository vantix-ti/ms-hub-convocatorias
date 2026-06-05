package cl.vantix.mshub.infrastructure.persistence.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
public class PostulacionPersistenceMapper {

    public Postulacion toDomain(PostulacionJpa jpa) {
        if (jpa == null) return null;
        return Postulacion.builder()
                .id(jpa.getId())
                .postulanteId(jpa.getPostulante().getId())
                .convocatoriaId(jpa.getConvocatoria().getId())
                .etapaActualId(jpa.getEtapaActual() != null ? jpa.getEtapaActual().getId() : null)
                .estado(EstadoPostulacion.valueOf(jpa.getEstado().getNombre()))
                .porcentajeAvance(jpa.getPorcentajeAvance())
                .enviadaEn(jpa.getEnviadaEn())
                .creadoEn(jpa.getCreadoEn()).actualizadoEn(jpa.getActualizadoEn())
                .build();
    }
}
