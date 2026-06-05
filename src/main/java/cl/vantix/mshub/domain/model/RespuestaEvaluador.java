package cl.vantix.mshub.domain.model;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RespuestaEvaluador {
    private Long id;
    private Long evaluacionId;
    private Long criterioId;
    private Double puntaje;
    private String comentario;
}
