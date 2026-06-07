package cl.vantix.mshub.infrastructure.web.dto.response;

import lombok.*;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ConfiguracionResponse {
    private Long institucionId;
    private Map<String, String> valores;
}
