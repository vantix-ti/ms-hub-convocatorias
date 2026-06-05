package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EtapaResponse {
    private Long id; private Long convocatoriaId; private String nombre;
    private String tipoEtapa; private int orden;
    private LocalDateTime fechaInicio; private LocalDateTime fechaFin;
    private String modoEvaluacion;
    private String instruccionesPostulante; private String instruccionesRevisor;
    private List<CampoFormularioResponse> campos;
    private List<CriterioEvaluacionResponse> criterios;
}
