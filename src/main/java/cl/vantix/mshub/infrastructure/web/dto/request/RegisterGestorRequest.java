package cl.vantix.mshub.infrastructure.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterGestorRequest {
    @NotBlank private String nombre;
    @NotBlank private String apellidoPaterno;
    private String apellidoMaterno;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 8) private String password;
    private String telefono;
    // Datos institución
    @NotBlank private String instNombre;
    private String instRut;
    private String instDireccion;
    private String instTelefono;
    @Email private String instEmail;
}
