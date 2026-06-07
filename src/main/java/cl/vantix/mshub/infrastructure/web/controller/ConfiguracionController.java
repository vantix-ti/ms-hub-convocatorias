package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.model.ConfiguracionPlataforma;
import cl.vantix.mshub.domain.port.in.ConfiguracionUseCase;
import cl.vantix.mshub.domain.port.in.InstitucionUseCase;
import cl.vantix.mshub.infrastructure.web.dto.response.ConfiguracionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestController @RequestMapping("/configuracion") @RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionUseCase useCase;
    private final InstitucionUseCase institucionUseCase;

    @GetMapping("/{institucionId}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConfiguracionResponse> obtener(@PathVariable Long institucionId) {
        Map<String, String> valores = useCase.obtenerPorInstitucion(institucionId)
                .stream().collect(Collectors.toMap(ConfiguracionPlataforma::getClave,
                        c -> c.getValor() != null ? c.getValor() : ""));
        return ResponseEntity.ok(ConfiguracionResponse.builder()
                .institucionId(institucionId).valores(valores).build());
    }

    @PutMapping("/{institucionId}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<ConfiguracionResponse> actualizar(@PathVariable Long institucionId,
                                                             @RequestBody Map<String, String> valores) {
        Map<String, String> updated = useCase.actualizarConfiguracion(institucionId, valores)
                .stream().collect(Collectors.toMap(ConfiguracionPlataforma::getClave,
                        c -> c.getValor() != null ? c.getValor() : ""));
        return ResponseEntity.ok(ConfiguracionResponse.builder()
                .institucionId(institucionId).valores(updated).build());
    }

    /** Endpoint público: obtener configuración por slug de institución (sin autenticación) */
    @GetMapping("/public/{slug}")
    public ResponseEntity<ConfiguracionResponse> obtenerPublico(@PathVariable String slug) {
        Long instId = institucionUseCase.obtenerPorSlug(slug).getId();
        Map<String, String> valores = useCase.obtenerPorInstitucion(instId)
                .stream().collect(Collectors.toMap(
                    cl.vantix.mshub.domain.model.ConfiguracionPlataforma::getClave,
                    c2 -> c2.getValor() != null ? c2.getValor() : ""));
        return ResponseEntity.ok(ConfiguracionResponse.builder()
                .institucionId(instId).valores(valores).build());
    }
}
