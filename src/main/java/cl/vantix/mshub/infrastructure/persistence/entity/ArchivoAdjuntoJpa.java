package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "archivos_adjuntos")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArchivoAdjuntoJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "respuesta_id")
    private RespuestaFormularioJpa respuesta;
    @Column(nullable = false) private String nombreOriginal;
    @Column(nullable = false) private String nombreAlmacenado;
    private String mimeType;
    private Long tamanio;
    private String url;
    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
}
