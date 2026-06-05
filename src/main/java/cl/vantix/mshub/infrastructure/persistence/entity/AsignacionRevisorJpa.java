package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "asignaciones_revisores")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AsignacionRevisorJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "revisor_id", nullable = false)
    private UsuarioJpa revisor;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "etapa_id", nullable = false)
    private EtapaJpa etapa;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "postulacion_id", nullable = false)
    private PostulacionJpa postulacion;
    @CreationTimestamp @Column(updatable = false) private LocalDateTime asignadoEn;
}
