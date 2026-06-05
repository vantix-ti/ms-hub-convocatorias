package cl.vantix.mshub.domain.model;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Etiqueta {
    private Long id;
    private String nombre;
    private String color;
}
