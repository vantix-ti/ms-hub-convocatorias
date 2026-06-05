package cl.vantix.mshub.infrastructure.web.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class PostulacionRequest {
    @NotNull private Long convocatoriaId;
}
