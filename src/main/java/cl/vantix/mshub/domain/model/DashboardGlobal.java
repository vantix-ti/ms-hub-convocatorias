package cl.vantix.mshub.domain.model;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardGlobal {
    private long convocatoriasActivas;
    private long totalPostulaciones;
    private long revisoresAsignados;
    private long seleccionados;
    private double seleccionadosPorcentaje;
    private Map<String, Long> postulacionesPorEstado;
    private List<UltimaPostulacion> ultimasPostulaciones;
    private List<PuntoTemporal> evolucionTemporal;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UltimaPostulacion {
        private Long id;
        private String postulanteNombre;
        private String convocatoriaTitulo;
        private java.time.LocalDateTime fecha;
        private String estado;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PuntoTemporal {
        private String fecha;
        private long enviadas;
        private long creadas;
    }
}
