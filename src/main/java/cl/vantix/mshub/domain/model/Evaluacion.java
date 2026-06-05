package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Evaluacion {
    private Long id;
    private Long postulacionId;
    private Long revisorId;
    private Long etapaId;
    private EstadoEvaluacion estado;
    private Double puntajeTotal;
    @Builder.Default private List<RespuestaEvaluador> respuestasEvaluador = new ArrayList<>();
    private boolean enviada;
    private LocalDateTime enviadaEn;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
