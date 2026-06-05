package cl.vantix.mshub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "convocatorias")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConvocatoriaJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;

    @Column(nullable = false) private String titulo;
    @Column(columnDefinition = "TEXT") private String descripcion;
    private String organizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoConvocatoriaJpa estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_registro_id", nullable = false)
    private TipoRegistroJpa tipoRegistro;

    @Column(nullable = false) @Builder.Default
    private int maxPostulacionesPorUsuario = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id")
    private UsuarioJpa creadoPor;

    @ManyToMany
    @JoinTable(name = "convocatoria_etiquetas",
            joinColumns = @JoinColumn(name = "convocatoria_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id"))
    @Builder.Default private Set<EtiquetaJpa> etiquetas = new HashSet<>();

    @OneToMany(mappedBy = "convocatoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC") @Builder.Default
    private List<EtapaJpa> etapas = new ArrayList<>();

    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
    @UpdateTimestamp private LocalDateTime actualizadoEn;
}
