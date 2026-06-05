package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Etapa {
    private Long id;
    private Long convocatoriaId;
    private String nombre;
    private TipoEtapa tipoEtapa;
    private int orden;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private ModoEvaluacion modoEvaluacion;
    private String instruccionesPostulante;
    private String instruccionesRevisor;
    private String mensajeEnvioEmail;
    private String mensajeSeleccionadoEmail;
    private String mensajeNoSeleccionadoEmail;
    @Builder.Default private List<CampoFormulario> campos = new ArrayList<>();
    @Builder.Default private List<CriterioEvaluacion> criterios = new ArrayList<>();
}
