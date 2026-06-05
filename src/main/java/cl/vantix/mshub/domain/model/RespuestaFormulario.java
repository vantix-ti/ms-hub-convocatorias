package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RespuestaFormulario {
    private Long id;
    private Long postulacionId;
    private Long campoId;
    private String valorTexto;
    private Double valorNumero;
    private LocalDate valorFecha;
    @Builder.Default private List<ArchivoAdjunto> archivos = new ArrayList<>();
}
