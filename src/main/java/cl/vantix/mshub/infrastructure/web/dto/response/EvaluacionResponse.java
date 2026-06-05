package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EvaluacionResponse {
    private Long id; private Long postulacionId; private Long revisorId;
    private Long etapaId; private String estado; private Double puntajeTotal;
    private List<RespuestaEvaluadorResponse> respuestasEvaluador;
    private boolean enviada; private LocalDateTime enviadaEn; private LocalDateTime creadoEn;
}
