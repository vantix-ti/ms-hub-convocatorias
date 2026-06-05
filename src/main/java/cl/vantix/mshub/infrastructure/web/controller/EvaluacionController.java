package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.*;
import cl.vantix.mshub.infrastructure.web.dto.request.*;
import cl.vantix.mshub.infrastructure.web.dto.response.*;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController @RequestMapping("/evaluaciones") @RequiredArgsConstructor
public class EvaluacionController {
    private final EvaluacionUseCase useCase;
    private final AuthUseCase authUseCase;
    private final WebMapper mapper;

    @PostMapping("/asignar") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,String>> asignar(@Valid @RequestBody AsignarRevisoresRequest req) {
        useCase.asignarRevisores(req.getPostulacionId(), req.getEtapaId(), req.getRevisorIds());
        return ResponseEntity.ok(Map.of("mensaje","Revisores asignados correctamente."));
    }

    @GetMapping("/mis-evaluaciones") @PreAuthorize("hasRole('REVISOR')")
    public ResponseEntity<List<EvaluacionResponse>> misEvaluaciones(@AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(useCase.obtenerMisEvaluaciones(uid).stream()
                .map(mapper::toEvaluacionResponse).collect(Collectors.toList()));
    }

    @PutMapping("/{id}") @PreAuthorize("hasRole('REVISOR')")
    public ResponseEntity<EvaluacionResponse> guardar(@PathVariable Long id,
                                                       @Valid @RequestBody EvaluacionRequest req,
                                                       @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toEvaluacionResponse(
                useCase.guardarEvaluacion(id, req.getComentario(), req.getPuntajesPorCriterio(), uid)));
    }

    @PostMapping("/{id}/finalizar") @PreAuthorize("hasRole('REVISOR')")
    public ResponseEntity<EvaluacionResponse> finalizar(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(mapper.toEvaluacionResponse(useCase.finalizarEvaluacion(id, uid)));
    }

    @PostMapping("/etapas/{etapaId}/notificar-resultados") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,String>> notificar(@PathVariable Long etapaId) {
        useCase.notificarResultados(etapaId);
        return ResponseEntity.ok(Map.of("mensaje","Notificaciones enviadas."));
    }
}
