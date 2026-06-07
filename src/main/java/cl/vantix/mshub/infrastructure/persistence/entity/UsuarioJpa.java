package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "usuarios")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @Column(nullable = false, length = 100) private String nombre;
    @Column(nullable = false, length = 100) private String apellidoPaterno;
    @Column(length = 100) private String apellidoMaterno;
    @Column(nullable = false, unique = true, length = 150) private String email;
    @Column(nullable = false) private String password;
    private boolean confirmado;
    @Column(nullable = false) @Builder.Default private boolean activo = true;
    @Column(length = 20) private String telefono;
    @Column(length = 500) private String avatarUrl;
    private String tokenConfirmacion;
    private LocalDateTime tokenExpiracion;
    private String tokenReset;
    private LocalDateTime tokenResetExpiracion;
    @Builder.Default private boolean twoFactorEnabled = false;
    @Builder.Default private int intentosFallidos = 0;
    private LocalDateTime bloqueadoHasta;
    @Column(name = "institucion_id") private Long institucionId;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id"))
    @Builder.Default private Set<RolJpa> roles = new HashSet<>();
    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
    @UpdateTimestamp private LocalDateTime actualizadoEn;
}
