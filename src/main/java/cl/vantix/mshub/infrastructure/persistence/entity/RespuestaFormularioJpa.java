package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name = "respuestas_formulario")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RespuestaFormularioJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "postulacion_id", nullable = false)
    private PostulacionJpa postulacion;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "campo_id", nullable = false)
    private CampoFormularioJpa campo;
    @Column(columnDefinition = "TEXT") private String valorTexto;
    private Double valorNumero;
    private LocalDate valorFecha;
    @OneToMany(mappedBy = "respuesta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default private List<ArchivoAdjuntoJpa> archivos = new ArrayList<>();
}
