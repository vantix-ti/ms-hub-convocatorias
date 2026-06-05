package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificacionResponse {
    private Long id; private String titulo; private String mensaje;
    private String tipo; private boolean leida; private LocalDateTime creadoEn;
}
