package cl.vantix.mshub.domain.model;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CampoFormulario {
    private Long id;
    private Long etapaId;
    private String nombre;
    private String mensajeAyuda;
    private TipoCampo tipoCampo;
    private boolean obligatorio;
    private int orden;
    @Builder.Default private List<String> opciones = new ArrayList<>();
    private boolean condicional;
    private Long campoCondicionId;
    private String valorCondicion;
    private int maxArchivos;
    private String formatosPermitidos;
    private int maxCaracteres;
}
