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

    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(useCase.listarTodos().stream()
                .map(mapper::toUsuarioResponse).collect(Collectors.toList()));
    }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crear(@RequestBody Map<String,String> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUsuarioResponse(
                useCase.crearUsuario(body.get("nombre"), body.get("apellidoPaterno"),
                        body.get("apellidoMaterno"), body.get("email"),
                        body.get("telefono"), Rol.valueOf(body.get("rol")),
                        body.get("password"))));   // null si no se envía → flujo email automático
    }

    @PutMapping("/me/password")
    public ResponseEntity<Map<String,String>> cambiarPassword(@RequestBody Map<String,String> body,
                                                              @AuthenticationPrincipal UserDetails ud) {
        useCase.cambiarPassword(ud.getUsername(), body.get("passwordActual"), body.get("nuevaPassword"));
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente."));
    }


    @GetMapping("/revisores") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> revisores() {
        return ResponseEntity.ok(useCase.listarPorRol(Rol.REVISOR).stream()
                .map(mapper::toUsuarioResponse).collect(Collectors.toList()));
    }

    @PutMapping("/{id}/rol") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> cambiarRol(@PathVariable Long id,
                                                      @RequestBody Map<String,String> body) {
        return ResponseEntity.ok(mapper.toUsuarioResponse(
                useCase.cambiarRol(id, Rol.valueOf(body.get("rol")))));
    }
}
