package cl.vantix.mshub.infrastructure.web.dto.request;
import cl.vantix.mshub.domain.model.ModoCriterio;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class CriterioEvaluacionRequest {
    @NotBlank private String nombre;
    private String descripcion;
    private Double ponderador;
    private int puntajeMinimo;
    @Min(1) private int puntajeMaximo = 100;
    private ModoCriterio modoCriterio;
    private String rubrica;
    private int orden;
}
