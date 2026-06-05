package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CampoFormularioResponse {
    private Long id; private Long etapaId; private String nombre;
    private String mensajeAyuda; private String tipoCampo; private boolean obligatorio;
    private int orden; private List<String> opciones; private int maxCaracteres;
    private String formatosPermitidos; private int maxArchivos;
}
