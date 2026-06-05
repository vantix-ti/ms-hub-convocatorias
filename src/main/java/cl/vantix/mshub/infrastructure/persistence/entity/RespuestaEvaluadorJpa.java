package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "respuestas_evaluador")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RespuestaEvaluadorJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "evaluacion_id", nullable = false)
    private EvaluacionJpa evaluacion;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "criterio_id", nullable = false)
    private CriterioEvaluacionJpa criterio;
    private Double puntaje;
    @Column(columnDefinition = "TEXT") private String comentario;
}
