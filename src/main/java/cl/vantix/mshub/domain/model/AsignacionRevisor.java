package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AsignacionRevisor {
    private Long id;
    private Long revisorId;
    private Long etapaId;
    private Long postulacionId;
    private LocalDateTime asignadoEn;
}
