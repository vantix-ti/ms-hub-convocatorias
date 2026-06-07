package cl.vantix.mshub.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "configuracion_plataforma",
        uniqueConstraints = @UniqueConstraint(columnNames = {"institucion_id", "clave"}))
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConfiguracionPlataformaJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @Column(name = "institucion_id", nullable = false) private Long institucionId;
    @Column(nullable = false, length = 100) private String clave;
    @Column(columnDefinition = "TEXT") private String valor;
    @UpdateTimestamp private LocalDateTime actualizadoEn;
}
