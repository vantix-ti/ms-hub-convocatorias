package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "postulaciones")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostulacionJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "postulante_id", nullable = false)
    private UsuarioJpa postulante;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "convocatoria_id", nullable = false)
    private ConvocatoriaJpa convocatoria;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "etapa_actual_id")
    private EtapaJpa etapaActual;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "estado_postulacion_id", nullable = false)
    private EstadoPostulacionJpa estado;
    @Column(nullable = false) @Builder.Default private int porcentajeAvance = 0;
    @OneToMany(mappedBy = "postulacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default private List<RespuestaFormularioJpa> respuestas = new ArrayList<>();
    @OneToMany(mappedBy = "postulacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default private List<EvaluacionJpa> evaluaciones = new ArrayList<>();
    private LocalDateTime enviadaEn;
    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
    @UpdateTimestamp private LocalDateTime actualizadoEn;
}
