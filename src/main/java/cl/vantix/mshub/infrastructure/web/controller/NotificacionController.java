package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.*;
import cl.vantix.mshub.infrastructure.web.dto.response.NotificacionResponse;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController @RequestMapping("/notificaciones") @RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionUseCase useCase;
    private final AuthUseCase authUseCase;
    private final WebMapper mapper;

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listar(@AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        return ResponseEntity.ok(useCase.listarPorUsuario(uid).stream()
                .map(mapper::toNotificacionResponse).collect(Collectors.toList()));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Map<String,String>> leer(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        useCase.marcarLeida(id, uid);
        return ResponseEntity.ok(Map.of("mensaje","Notificación marcada como leída."));
    }

    @PutMapping("/leer-todas")
    public ResponseEntity<Map<String,String>> leerTodas(@AuthenticationPrincipal UserDetails ud) {
        Long uid = authUseCase.obtenerPorEmail(ud.getUsername()).getId();
        useCase.marcarTodasLeidas(uid);
        return ResponseEntity.ok(Map.of("mensaje","Todas las notificaciones marcadas como leídas."));
    }
}
