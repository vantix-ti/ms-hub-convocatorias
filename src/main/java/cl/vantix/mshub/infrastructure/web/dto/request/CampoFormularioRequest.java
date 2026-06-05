package cl.vantix.mshub.infrastructure.web.dto.request;
import cl.vantix.mshub.domain.model.TipoCampo;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor
public class CampoFormularioRequest {
    @NotBlank private String nombre;
    private String mensajeAyuda;
    @NotNull private TipoCampo tipoCampo;
    private boolean obligatorio;
    @Min(1) private int orden;
    private List<String> opciones;
    private int maxCaracteres;
    private String formatosPermitidos;
    private int maxArchivos = 1;
}
