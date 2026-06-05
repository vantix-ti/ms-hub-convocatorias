package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.model.EstadoConvocatoria;
import cl.vantix.mshub.domain.port.in.ConvocatoriaUseCase;
import cl.vantix.mshub.infrastructure.web.dto.request.ConvocatoriaRequest;
import cl.vantix.mshub.infrastructure.web.dto.response.ConvocatoriaResponse;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController @RequestMapping("/convocatorias") @RequiredArgsConstructor
public class ConvocatoriaController {

    private final ConvocatoriaUseCase useCase;
    private final cl.vantix.mshub.domain.port.in.AuthUseCase authUseCase;
    private final WebMapper mapper;

    @GetMapping
    public ResponseEntity<List<ConvocatoriaResponse>> listarPublicas() {
        return ResponseEntity.ok(useCase.listarPublicas().stream()
                .map(mapper::toConvocatoriaResponse).collect(Collectors.toList()));
    }

    @GetMapping("/todas") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConvocatoriaResponse>> listarTodas() {
        return ResponseEntity.ok(useCase.listarTodas().stream()
                .map(mapper::toConvocatoriaResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConvocatoriaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toConvocatoriaResponse(useCase.obtenerPorId(id)));
    }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> crear(
            @Valid @RequestBody ConvocatoriaRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.toConvocatoriaResponse(useCase.crear(
                        req.getTitulo(), req.getDescripcion(), req.getOrganizacion(),
                        req.getFechaInicio(), req.getFechaFin(), req.getImagen(), uid)));
    }

    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConvocatoriaRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toConvocatoriaResponse(
                useCase.actualizar(id, req.getTitulo(), req.getDescripcion(), req.getOrganizacion(),
                        req.getFechaInicio(), req.getFechaFin(), req.getImagen(), uid)));
    }

    @PutMapping("/{id}/estado") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConvocatoriaResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toConvocatoriaResponse(
                useCase.cambiarEstado(id, EstadoConvocatoria.valueOf(body.get("estado")), uid)));
    }

    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        useCase.eliminar(id, uid);
        return ResponseEntity.noContent().build();
    }
}
