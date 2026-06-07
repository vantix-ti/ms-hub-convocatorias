package cl.vantix.mshub.infrastructure.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class ConvocatoriaRequest {
    @NotBlank private String titulo;
    private String descripcion;
    private String organizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String imagen;  // base64 data URL
    private List<DocumentoRequest> documentos = new ArrayList<>();

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class DocumentoRequest {
        @NotBlank private String nombre;
        @Size(max = 100) private String descripcion;
        @NotBlank private String contenido;  // base64
        private String tipoMime;
        private Long tamanio;
    }
}
