package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private Set<String> roles;
    private String telefono;
    private boolean activo;
    private boolean confirmado;
    private LocalDateTime creadoEn;
}
