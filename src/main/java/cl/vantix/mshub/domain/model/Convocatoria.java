package cl.vantix.mshub.domain.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Convocatoria {
    private Long id;
    private String titulo;
    private String descripcion;
    private String organizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String imagen;
    private EstadoConvocatoria estado;
    private TipoRegistro tipoRegistro;
    private int maxPostulacionesPorUsuario;
    private Long creadoPorId;
    @Builder.Default private Set<Etiqueta> etiquetas = new HashSet<>();
    @Builder.Default private List<Etapa> etapas = new ArrayList<>();
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
