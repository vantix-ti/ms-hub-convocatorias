package cl.vantix.mshub.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ConfiguracionPlataforma {
    private Long id;
    private Long institucionId;
    private String clave;
    private String valor;
    private LocalDateTime actualizadoEn;
}
