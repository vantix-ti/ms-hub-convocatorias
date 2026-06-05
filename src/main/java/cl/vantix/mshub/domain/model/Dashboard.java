package cl.vantix.mshub.domain.model;
import lombok.*;
import java.util.Map;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Dashboard {
    private Long convocatoriaId;
    private String convocatoriaNombre;
    private long totalPostulaciones;
    private long postulacionesEnviadas;
    private long postulacionesEnEvaluacion;
    private long postulacionesSeleccionadas;
    private long postulacionesNoSeleccionadas;
    private Double promedioGeneral;
    private Map<String, Long> postulacionesPorEstado;
}
