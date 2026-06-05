package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PostulacionResponse {
    private Long id; private Long postulanteId; private Long convocatoriaId;
    private String estado; private int porcentajeAvance;
    private List<RespuestaFormularioResponse> respuestas;
    private LocalDateTime enviadaEn; private LocalDateTime creadoEn;
}
