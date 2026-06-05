package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "evaluaciones")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EvaluacionJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "postulacion_id", nullable = false)
    private PostulacionJpa postulacion;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "revisor_id", nullable = false)
    private UsuarioJpa revisor;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "etapa_id", nullable = false)
    private EtapaJpa etapa;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "estado_evaluacion_id", nullable = false)
    private EstadoEvaluacionJpa estado;
    private Double puntajeTotal;
    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default private List<RespuestaEvaluadorJpa> respuestasEvaluador = new ArrayList<>();
    @Builder.Default private boolean enviada = false;
    private LocalDateTime enviadaEn;
    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
    @UpdateTimestamp private LocalDateTime actualizadoEn;
}
