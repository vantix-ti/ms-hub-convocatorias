package cl.vantix.mshub.domain.port.in;
import cl.vantix.mshub.domain.model.*;
import java.time.LocalDateTime;
import java.util.List;

public interface EtapaUseCase {
    List<Etapa> listarPorConvocatoria(Long convocatoriaId);
    Etapa obtenerPorId(Long id);
    Etapa crear(Long convocatoriaId, String nombre, TipoEtapa tipo, int orden,
                LocalDateTime fechaInicio, LocalDateTime fechaFin,
                ModoEvaluacion modoEvaluacion, String instruccionesPostulante,
                String instruccionesRevisor);
    Etapa actualizar(Long id, String nombre, TipoEtapa tipo, int orden,
                     LocalDateTime fechaInicio, LocalDateTime fechaFin,
                     ModoEvaluacion modoEvaluacion, String instruccionesPostulante,
                     String instruccionesRevisor);
    void eliminar(Long id);

    CampoFormulario agregarCampo(Long etapaId, String nombre, String mensajeAyuda,
                                  TipoCampo tipo, boolean obligatorio, int orden,
                                  List<String> opciones, int maxCaracteres,
                                  String formatosPermitidos, int maxArchivos);
    CampoFormulario actualizarCampo(Long campoId, String nombre, String mensajeAyuda,
                                     TipoCampo tipo, boolean obligatorio, int orden,
                                     List<String> opciones, int maxCaracteres,
                                     String formatosPermitidos, int maxArchivos);
    void eliminarCampo(Long campoId);

    CriterioEvaluacion agregarCriterio(Long etapaId, String nombre, String descripcion,
                                        Double ponderador, int puntajeMin, int puntajeMax,
                                        ModoCriterio modo, String rubrica, int orden);
    CriterioEvaluacion actualizarCriterio(Long criterioId, String nombre, String descripcion,
                                           Double ponderador, int puntajeMin, int puntajeMax,
                                           ModoCriterio modo, String rubrica, int orden);
    void eliminarCriterio(Long criterioId);
}
