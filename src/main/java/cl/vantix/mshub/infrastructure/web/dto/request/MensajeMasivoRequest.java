package cl.vantix.mshub.infrastructure.web.dto.request;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class MensajeMasivoRequest {
    /** TODOS | POSTULANTE | REVISOR */
    private String destinatarios;
    private String titulo;
    private String mensaje;
}
