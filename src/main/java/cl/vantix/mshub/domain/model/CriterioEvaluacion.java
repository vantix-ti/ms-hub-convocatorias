package cl.vantix.mshub.domain.model;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CriterioEvaluacion {
    private Long id;
    private Long etapaId;
    private String nombre;
    private String descripcion;
    private Double ponderador;
    private int puntajeMinimo;
    private int puntajeMaximo;
    private ModoCriterio modoCriterio;
    private String rubrica;
    private int orden;
}
