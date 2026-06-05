package cl.vantix.mshub.infrastructure.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class ConvocatoriaRequest {
    @NotBlank private String titulo;
    private String descripcion;
    private String organizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String imagen;
}
