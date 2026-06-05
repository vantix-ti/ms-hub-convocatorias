package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RespuestaEvaluadorResponse {
    private Long id; private Long criterioId; private Double puntaje; private String comentario;
}
