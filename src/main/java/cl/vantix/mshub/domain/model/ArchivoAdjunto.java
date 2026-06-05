package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ArchivoAdjunto {
    private Long id;
    private Long respuestaId;
    private String nombreOriginal;
    private String nombreAlmacenado;
    private String mimeType;
    private Long tamanio;
    private String url;
    private LocalDateTime creadoEn;
}
