package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "etapas")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EtapaJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "convocatoria_id", nullable = false)
    private ConvocatoriaJpa convocatoria;
    @Column(nullable = false) private String nombre;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tipo_etapa_id", nullable = false)
    private TipoEtapaJpa tipoEtapa;
    @Column(nullable = false) private int orden;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "modo_evaluacion_id")
    private ModoEvaluacionJpa modoEvaluacion;
    @Column(columnDefinition = "TEXT") private String instruccionesPostulante;
    @Column(columnDefinition = "TEXT") private String instruccionesRevisor;
    @Column(columnDefinition = "TEXT") private String mensajeEnvioEmail;
    @Column(columnDefinition = "TEXT") private String mensajeSeleccionadoEmail;
    @Column(columnDefinition = "TEXT") private String mensajeNoSeleccionadoEmail;
    @OneToMany(mappedBy = "etapa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC") @Builder.Default private List<CampoFormularioJpa> campos = new ArrayList<>();
    @OneToMany(mappedBy = "etapa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC") @Builder.Default private List<CriterioEvaluacionJpa> criterios = new ArrayList<>();
}
