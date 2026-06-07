package cl.vantix.mshub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "instituciones")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InstitucionJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @Column(nullable = false, length = 255) private String nombre;
    @Column(length = 20) private String rut;
    @Column(length = 500) private String direccion;
    @Column(length = 50) private String telefono;
    @Column(length = 255) private String email;
    @Column(columnDefinition = "TEXT") private String logoUrl;
    @Column(length = 100, unique = true) private String slug;
    @Column(nullable = false) @Builder.Default private boolean activo = true;
    @CreationTimestamp @Column(updatable = false) private LocalDateTime creadoEn;
    @UpdateTimestamp private LocalDateTime actualizadoEn;
}
