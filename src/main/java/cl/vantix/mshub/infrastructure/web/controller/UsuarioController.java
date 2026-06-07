package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.port.in.UsuarioUseCase;
import cl.vantix.mshub.infrastructure.web.dto.response.UsuarioResponse;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController @RequestMapping("/usuarios") @RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioUseCase useCase;
    private final WebMapper mapper;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> perfil(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(mapper.toUsuarioResponse(useCase.obtenerPorEmail(ud.getUsername())));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizar(@RequestBody Map<String,String> body,
                                                      @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(mapper.toUsuarioResponse(useCase.actualizarPerfil(
                ud.getUsername(), body.get("nombre"), body.get("apellidoPaterno"),
                body.get("apellidoMaterno"), body.get("telefono"))));
    }

    /** ADMIN y GESTOR pueden listar usuarios */
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(useCase.listarTodos().stream()
                .map(mapper::toUsuarioResponse).collect(Collectors.toList()));
    }

    /**
     * Crear usuario.
     * - ADMIN: puede crear ADMIN, GESTOR, REVISOR (sin POSTULANTE desde panel).
     * - GESTOR: solo puede crear GESTOR o REVISOR.
     */
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<?> crear(@RequestBody Map<String,String> body,
                                   @AuthenticationPrincipal UserDetails ud) {
        String targetRolStr = body.get("rol");
        if (targetRolStr == null || targetRolStr.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'rol' es requerido."));
        }

        // Validar que el rol destino es válido
        Rol targetRol;
        try { targetRol = Rol.valueOf(targetRolStr); }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rol inválido: " + targetRolStr));
        }

        // Si el caller es GESTOR, solo puede crear GESTOR o REVISOR
        boolean isGestor = ud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR"));
        if (isGestor && targetRol != Rol.GESTOR && targetRol != Rol.REVISOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Un Gestor solo puede crear usuarios con rol GESTOR o REVISOR."));
        }

        Long institucionId = null;
        if (body.get("institucionId") != null && !body.get("institucionId").isBlank()) {
            try { institucionId = Long.parseLong(body.get("institucionId")); }
            catch (NumberFormatException ignored) {}
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUsuarioResponse(
                useCase.crearUsuario(body.get("nombre"), body.get("apellidoPaterno"),
                        body.get("apellidoMaterno"), body.get("email"),
                        body.get("telefono"), targetRol,
                        body.get("password"), institucionId)));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Map<String,String>> cambiarPassword(@RequestBody Map<String,String> body,
                                                              @AuthenticationPrincipal UserDetails ud) {
        useCase.cambiarPassword(ud.getUsername(), body.get("passwordActual"), body.get("nuevaPassword"));
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente."));
    }

    /** ADMIN y GESTOR pueden ver la lista de revisores */
    @GetMapping("/revisores") @PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
    public ResponseEntity<List<UsuarioResponse>> revisores() {
        return ResponseEntity.ok(useCase.listarPorRol(Rol.REVISOR).stream()
                .map(mapper::toUsuarioResponse).collect(Collectors.toList()));
    }

    /** Solo ADMIN puede cambiar rol */
    @PutMapping("/{id}/rol") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> cambiarRol(@PathVariable Long id,
                                                      @RequestBody Map<String,String> body) {
        return ResponseEntity.ok(mapper.toUsuarioResponse(
                useCase.cambiarRol(id, Rol.valueOf(body.get("rol")))));
    }
}
