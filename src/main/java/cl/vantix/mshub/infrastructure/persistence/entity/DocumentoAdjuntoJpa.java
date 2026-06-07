package cl.vantix.mshub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "documento_adjunto")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DocumentoAdjuntoJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;

    @Column(name = "convocatoria_id", nullable = false)
    private Long convocatoriaId;

    @Column(nullable = false)       private String nombre;
    @Column(length = 100)           private String descripcion;
    @Column(columnDefinition = "TEXT", nullable = false) private String contenido;
    @Column(name = "tipo_mime")     private String tipoMime;
    private Long tamanio;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;
}
