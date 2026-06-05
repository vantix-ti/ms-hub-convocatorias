package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CriterioEvaluacionResponse {
    private Long id; private Long etapaId; private String nombre;
    private String descripcion; private Double ponderador;
    private int puntajeMinimo; private int puntajeMaximo;
    private String modoCriterio; private String rubrica; private int orden;
}
