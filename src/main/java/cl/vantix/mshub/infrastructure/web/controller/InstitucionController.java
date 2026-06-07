package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.model.Institucion;
import cl.vantix.mshub.domain.port.in.InstitucionUseCase;
import cl.vantix.mshub.infrastructure.web.dto.request.CreateInstitucionRequest;
import cl.vantix.mshub.infrastructure.web.dto.response.InstitucionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping("/instituciones") @RequiredArgsConstructor
public class InstitucionController {

    private final InstitucionUseCase useCase;

    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<InstitucionResponse>> listar() {
        return ResponseEntity.ok(useCase.listarTodas().stream()
                .map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<InstitucionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(useCase.obtenerPorId(id)));
    }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstitucionResponse> crear(@Valid @RequestBody CreateInstitucionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(useCase.crear(req.getNombre(), req.getRut(),
                        req.getDireccion(), req.getTelefono(), req.getEmail(), req.getLogoUrl())));
    }

    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<InstitucionResponse> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody CreateInstitucionRequest req) {
        return ResponseEntity.ok(toResponse(useCase.actualizar(id, req.getNombre(), req.getRut(),
                req.getDireccion(), req.getTelefono(), req.getEmail(), req.getLogoUrl())));
    }

    private InstitucionResponse toResponse(Institucion inst) {
        return InstitucionResponse.builder()
                .id(inst.getId()).nombre(inst.getNombre()).rut(inst.getRut())
                .direccion(inst.getDireccion()).telefono(inst.getTelefono())
                .email(inst.getEmail()).logoUrl(inst.getLogoUrl())
                .activo(inst.isActivo()).creadoEn(inst.getCreadoEn()).build();
    }
}
