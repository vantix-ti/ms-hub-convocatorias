package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.Evaluacion;
import java.util.List;
import java.util.Map;

public interface EvaluacionUseCase {
    void asignarRevisores(Long postulacionId, Long etapaId, List<Long> revisorIds);
    List<Evaluacion> obtenerMisEvaluaciones(Long revisorId);
    Evaluacion guardarEvaluacion(Long evaluacionId, String comentario,
                                  Map<Long, Double> puntajesPorCriterio, Long revisorId);
    Evaluacion finalizarEvaluacion(Long evaluacionId, Long revisorId);
    void notificarResultados(Long etapaId);
}
