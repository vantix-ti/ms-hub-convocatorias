package cl.vantix.mshub.infrastructure.web.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EtiquetaResponse {
    private Long id; private String nombre; private String color;
}
