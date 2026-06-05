package cl.vantix.mshub.infrastructure.web.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor
public class RespuestaRequest {
    @NotNull private Long campoId;
    private String valorTexto;
    private Double valorNumero;
    private LocalDate valorFecha;
}
