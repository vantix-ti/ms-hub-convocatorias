package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.DashboardUseCase;
import cl.vantix.mshub.infrastructure.web.dto.response.DashboardResponse;
import cl.vantix.mshub.infrastructure.web.dto.response.DashboardGlobalResponse;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/dashboard") @RequiredArgsConstructor
public class DashboardController {
    private final DashboardUseCase useCase;
    private final WebMapper mapper;

    @GetMapping("/convocatoria/{id}") @PreAuthorize("hasAnyRole('ADMIN','REVISOR')")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDashboardResponse(useCase.obtenerDashboard(id)));
    }

    @GetMapping("/global") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardGlobalResponse> getDashboardGlobal() {
        return ResponseEntity.ok(mapper.toDashboardGlobalResponse(useCase.obtenerDashboardGlobal()));
    }
}
