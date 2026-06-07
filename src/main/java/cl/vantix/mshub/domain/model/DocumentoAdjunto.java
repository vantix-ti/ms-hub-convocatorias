package cl.vantix.mshub.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DocumentoAdjunto {
    private Long id;
    private Long convocatoriaId;
    private String nombre;
    private String descripcion;   // máx 100 chars
    private String contenido;     // base64
    private String tipoMime;
    private Long tamanio;
    private LocalDateTime creadoEn;
}
