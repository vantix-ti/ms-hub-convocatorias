package cl.vantix.mshub.infrastructure.web.dto.request;
import lombok.*;
import java.util.Map;
@Data @NoArgsConstructor @AllArgsConstructor
public class EvaluacionRequest {
    private String comentario;
    private Map<Long, Double> puntajesPorCriterio;
}
