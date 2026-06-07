package cl.vantix.mshub.infrastructure.web.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InstitucionResponse {
    private Long id;
    private String nombre;
    private String rut;
    private String direccion;
    private String telefono;
    private String email;
    private String logoUrl;
    private boolean activo;
    private LocalDateTime creadoEn;
}
