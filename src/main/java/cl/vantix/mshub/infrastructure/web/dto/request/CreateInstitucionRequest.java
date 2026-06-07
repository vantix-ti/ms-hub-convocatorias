package cl.vantix.mshub.infrastructure.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateInstitucionRequest {
    @NotBlank private String nombre;
    private String rut;
    private String direccion;
    private String telefono;
    @Email private String email;
    private String logoUrl;
    private Boolean activo;
}
