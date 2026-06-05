package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Postulacion {
    private Long id;
    private Long postulanteId;
    private Long convocatoriaId;
    private Long etapaActualId;
    private EstadoPostulacion estado;
    private int porcentajeAvance;
    @Builder.Default private List<RespuestaFormulario> respuestas = new ArrayList<>();
    @Builder.Default private List<Evaluacion> evaluaciones = new ArrayList<>();
    private LocalDateTime enviadaEn;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
