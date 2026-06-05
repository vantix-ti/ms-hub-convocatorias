package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RespuestaFormularioResponse {
    private Long id; private Long campoId;
    private String valorTexto; private Double valorNumero; private LocalDate valorFecha;
    private List<ArchivoAdjuntoResponse> archivos;
}
