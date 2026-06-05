package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ArchivoAdjuntoResponse {
    private Long id; private String nombreOriginal; private String mimeType;
    private Long tamanio; private String url; private LocalDateTime creadoEn;
}
