package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.DashboardUseCase;
import cl.vantix.mshub.domain.port.in.UsuarioUseCase;
import cl.vantix.mshub.infrastructure.web.dto.response.DashboardResponse;
import cl.vantix.mshub.infrastructure.web.dto.response.DashboardGlobalResponse;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/dashboard") @RequiredArgsConstructor
public class DashboardController {
    private final DashboardUseCase useCase;
    private final UsuarioUseCase usuarioUseCase;
    private final WebMapper mapper;

    @GetMapping("/convocatoria/{id}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR','REVISOR')")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDashboardResponse(useCase.obtenerDashboard(id)));
    }

    /**
     * Dashboard global:
     * - ADMIN (Vantix): ve datos de todas las instituciones.
     * - GESTOR: ve solo datos de su propia institución.
     */
    @GetMapping("/global") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<DashboardGlobalResponse> getDashboardGlobal(
            @AuthenticationPrincipal UserDetails ud) {

        boolean isGestor = ud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR"));

        DashboardGlobalResponse response;
        if (isGestor) {
            Long instId = usuarioUseCase.obtenerPorEmail(ud.getUsername()).getInstitucionId();
            response = instId != null
                    ? mapper.toDashboardGlobalResponse(useCase.obtenerDashboardPorInstitucion(instId))
                    : mapper.toDashboardGlobalResponse(useCase.obtenerDashboardGlobal()); // fallback vacío
        } else {
            response = mapper.toDashboardGlobalResponse(useCase.obtenerDashboardGlobal());
        }
        return ResponseEntity.ok(response);
    }
}
