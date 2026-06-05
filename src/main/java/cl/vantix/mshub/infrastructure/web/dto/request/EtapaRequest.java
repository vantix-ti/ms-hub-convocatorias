package cl.vantix.mshub.infrastructure.web.dto.request;
import cl.vantix.mshub.domain.model.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor
public class EtapaRequest {
    @NotBlank private String nombre;
    @NotNull private TipoEtapa tipoEtapa;
    @Min(1) private int orden;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private ModoEvaluacion modoEvaluacion;
    private String instruccionesPostulante;
    private String instruccionesRevisor;
}
