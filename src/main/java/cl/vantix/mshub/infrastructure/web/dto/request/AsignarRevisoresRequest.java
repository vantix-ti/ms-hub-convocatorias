package cl.vantix.mshub.infrastructure.web.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor
public class AsignarRevisoresRequest {
    @NotNull private Long postulacionId;
    @NotNull private Long etapaId;
    @NotEmpty private List<Long> revisorIds;
}
