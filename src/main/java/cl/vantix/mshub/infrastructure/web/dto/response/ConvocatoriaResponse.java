package cl.vantix.mshub.infrastructure.web.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ConvocatoriaResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private String organizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String imagen;
    private String estado;
    private String tipoRegistro;
    private int maxPostulacionesPorUsuario;
    private Set<EtiquetaResponse> etiquetas;
    private List<EtapaResponse> etapas;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
