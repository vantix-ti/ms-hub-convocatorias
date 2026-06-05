package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.BusinessException;
import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.exception.UnauthorizedException;
import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.domain.port.in.EvaluacionUseCase;
import cl.vantix.mshub.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EvaluacionUseCaseImpl implements EvaluacionUseCase {

    private final EvaluacionPersistencePort evaluacionPort;
    private final AsignacionRevisorPersistencePort asignacionPort;
    private final PostulacionPersistencePort postulacionPort;
    private final NotificacionPersistencePort notificacionPort;
    private final UsuarioPersistencePort usuarioPort;

    @Override @Transactional
    public void asignarRevisores(Long postulacionId, Long etapaId, List<Long> revisorIds) {
        postulacionPort.findById(postulacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada."));
        for (Long revisorId : revisorIds) {
            if (asignacionPort.existsByPostulacionIdAndRevisorIdAndEtapaId(postulacionId, revisorId, etapaId))
                continue;
            asignacionPort.save(AsignacionRevisor.builder()
                    .postulacionId(postulacionId).revisorId(revisorId).etapaId(etapaId)
                    .asignadoEn(LocalDateTime.now()).build());
            if (!evaluacionPort.existsByPostulacionIdAndRevisorIdAndEtapaId(postulacionId, revisorId, etapaId))
                evaluacionPort.save(Evaluacion.builder()
                        .postulacionId(postulacionId).revisorId(revisorId).etapaId(etapaId)
                        .estado(EstadoEvaluacion.PENDIENTE).enviada(false).build());
        }
    }

    @Override
    public List<Evaluacion> obtenerMisEvaluaciones(Long revisorId) {
        return evaluacionPort.findByRevisorId(revisorId);
    }

    @Override @Transactional
    public Evaluacion guardarEvaluacion(Long evaluacionId, String comentario,
                                        Map<Long, Double> puntajesPorCriterio, Long revisorId) {
        Evaluacion eval = evaluacionPort.findById(evaluacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada."));
        if (!eval.getRevisorId().equals(revisorId))
            throw new UnauthorizedException("No tienes permiso para evaluar esta postulación.");
        if (eval.getEstado() == EstadoEvaluacion.FINALIZADA)
            throw new BusinessException("La evaluación ya está finalizada.");
        eval.setEstado(EstadoEvaluacion.EN_PROGRESO);
        List<RespuestaEvaluador> respuestas = new java.util.ArrayList<>();
        if (puntajesPorCriterio != null) {
            puntajesPorCriterio.forEach((criterioId, puntaje) ->
                    respuestas.add(RespuestaEvaluador.builder()
                            .evaluacionId(evaluacionId).criterioId(criterioId)
                            .puntaje(puntaje).comentario(comentario).build()));
        }
        eval.setRespuestasEvaluador(respuestas);
        if (!respuestas.isEmpty()) {
            double promedio = respuestas.stream()
                    .filter(r -> r.getPuntaje() != null)
                    .mapToDouble(RespuestaEvaluador::getPuntaje).average().orElse(0.0);
            eval.setPuntajeTotal(promedio);
        }
        return evaluacionPort.save(eval);
    }

    @Override @Transactional
    public Evaluacion finalizarEvaluacion(Long evaluacionId, Long revisorId) {
        Evaluacion eval = evaluacionPort.findById(evaluacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada."));
        if (!eval.getRevisorId().equals(revisorId))
            throw new UnauthorizedException("No tienes permiso para finalizar esta evaluación.");
        eval.setEstado(EstadoEvaluacion.FINALIZADA);
        eval.setEnviada(true);
        eval.setEnviadaEn(LocalDateTime.now());
        return evaluacionPort.save(eval);
    }

    @Override @Transactional
    public void notificarResultados(Long etapaId) {
        List<Evaluacion> evaluaciones = evaluacionPort.findByEtapaId(etapaId);
        evaluaciones.forEach(eval -> {
            postulacionPort.findById(eval.getPostulacionId()).ifPresent(p -> {
                usuarioPort.findById(p.getPostulanteId()).ifPresent(u -> {
                    notificacionPort.save(Notificacion.builder()
                            .destinatarioId(u.getId())
                            .tipo(TipoNotificacion.SISTEMA)
                            .titulo("Resultado de evaluación")
                            .mensaje("Tu postulación ha sido evaluada. Puntaje: " + eval.getPuntajeTotal())
                            .leida(false).build());
                });
            });
        });
    }
}
