package cl.vantix.mshub.infrastructure.persistence.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class EvaluacionPersistenceMapper {

    public Evaluacion toDomain(EvaluacionJpa jpa) {
        if (jpa == null) return null;
        return Evaluacion.builder()
                .id(jpa.getId())
                .postulacionId(jpa.getPostulacion().getId())
                .revisorId(jpa.getRevisor().getId())
                .etapaId(jpa.getEtapa().getId())
                .estado(EstadoEvaluacion.valueOf(jpa.getEstado().getNombre()))
                .puntajeTotal(jpa.getPuntajeTotal())
                .enviada(jpa.isEnviada()).enviadaEn(jpa.getEnviadaEn())
                .respuestasEvaluador(jpa.getRespuestasEvaluador().stream()
                        .map(r -> RespuestaEvaluador.builder()
                                .id(r.getId()).evaluacionId(r.getEvaluacion().getId())
                                .criterioId(r.getCriterio().getId())
                                .puntaje(r.getPuntaje()).comentario(r.getComentario()).build())
                        .collect(Collectors.toList()))
                .creadoEn(jpa.getCreadoEn()).actualizadoEn(jpa.getActualizadoEn())
                .build();
    }
}
