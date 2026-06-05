package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardGlobalResponse {
    private long convocatoriasActivas;
    private long totalPostulaciones;
    private long revisoresAsignados;
    private long seleccionados;
    private double seleccionadosPorcentaje;
    private Map<String, Long> postulacionesPorEstado;
    private List<UltimaPostulacionDto> ultimasPostulaciones;
    private List<PuntoTemporalDto> evolucionTemporal;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UltimaPostulacionDto {
        private Long id;
        private String postulanteNombre;
        private String convocatoriaTitulo;
        private LocalDateTime fecha;
        private String estado;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PuntoTemporalDto {
        private String fecha;
        private long enviadas;
        private long creadas;
    }
}
