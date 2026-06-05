package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.util.Set;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo;
    private String email;
    private Set<String> roles;
}
