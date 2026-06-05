package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String password;
    @Builder.Default private Set<Rol> roles = new HashSet<>();
    private boolean confirmado;
    private boolean activo;
    private String telefono;
    private String avatarUrl;
    private String tokenConfirmacion;
    private LocalDateTime tokenExpiracion;
    private String tokenReset;
    private LocalDateTime tokenResetExpiracion;
    private boolean twoFactorEnabled;
    private int intentosFallidos;
    private LocalDateTime bloqueadoHasta;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    public boolean hasRole(Rol rol) { return roles.contains(rol); }
    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno +
               (apellidoMaterno != null ? " " + apellidoMaterno : "");
    }
}
