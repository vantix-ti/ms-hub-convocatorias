package cl.vantix.mshub.infrastructure.persistence.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity @Table(name = "campos_formulario")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CampoFormularioJpa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "etapa_id", nullable = false)
    private EtapaJpa etapa;
    @Column(nullable = false) private String nombre;
    private String mensajeAyuda;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "tipo_campo_id", nullable = false)
    private TipoCampoJpa tipoCampo;
    @Column(nullable = false) @Builder.Default private boolean obligatorio = false;
    @Column(nullable = false) private int orden;
    @ElementCollection
    @CollectionTable(name = "campo_opciones", joinColumns = @JoinColumn(name = "campo_id"))
    @Column(name = "opcion") @Builder.Default private List<String> opciones = new ArrayList<>();
    @Builder.Default private boolean condicional = false;
    private Long campoCondicionId;
    private String valorCondicion;
    @Builder.Default private int maxArchivos = 1;
    private String formatosPermitidos;
    @Builder.Default private int maxCaracteres = 0;
}
