package cl.vantix.mshub.infrastructure.web.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank private String nombre;
    @NotBlank private String apellidoPaterno;
    private String apellidoMaterno;
    @NotBlank @Email private String email;
    @NotBlank @Size(min=8) private String password;
    private String telefono;
    // Datos empresa (postulante tipo empresa — todos opcionales)
    private String empresaNombre;
    private String empresaRut;
    private String empresaDireccion;
    private String empresaTelefono;
    @Email private String empresaEmail;
}
