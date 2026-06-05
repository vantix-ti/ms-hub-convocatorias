package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Notificacion {
    private Long id;
    private Long destinatarioId;
    private TipoNotificacion tipo;
    private String titulo;
    private String mensaje;
    private boolean leida;
    private LocalDateTime creadoEn;
}
