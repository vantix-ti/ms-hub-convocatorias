package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "criterios_evaluacion")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CriterioEvaluacionJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "etapa_id", nullable = false)
    private EtapaJpa etapa;
    @Column(nullable = false) private String nombre;
    @Column(columnDefinition = "TEXT") private String descripcion;
    private Double ponderador;
    @Builder.Default private int puntajeMinimo = 0;
    @Builder.Default private int puntajeMaximo = 100;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "modo_criterio_id")
    private ModoCriterioJpa modoCriterio;
    @Column(columnDefinition = "TEXT") private String rubrica;
    @Builder.Default private int orden = 0;
}
