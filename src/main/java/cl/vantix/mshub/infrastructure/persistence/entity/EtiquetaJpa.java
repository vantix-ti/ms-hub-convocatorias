package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "etiquetas")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EtiquetaJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @Column(nullable = false, unique = true) private String nombre;
    private String color;
}
