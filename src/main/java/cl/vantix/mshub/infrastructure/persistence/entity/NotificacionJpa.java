package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "notificaciones")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NotificacionJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "destinatario_id", nullable = false)
    private UsuarioJpa destinatario;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tipo_notificacion_id", nullable = false)
    private TipoNotificacionJpa tipoNotificacion;
    @Column(nullable = false) private String titulo;
    @Column(columnDefinition = "TEXT") private String mensaje;
    @Builder.Default private boolean leida = false;
    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
}
