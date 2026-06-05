package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.util.Map;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponse {
    private Long convocatoriaId; private String convocatoriaNombre;
    private long totalPostulaciones; private long postulacionesEnviadas;
    private long postulacionesEnEvaluacion; private long postulacionesSeleccionadas;
    private long postulacionesNoSeleccionadas; private Double promedioGeneral;
    private Map<String, Long> postulacionesPorEstado;
}
