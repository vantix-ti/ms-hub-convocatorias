package cl.vantix.mshub.infrastructure.web.controller;

import cl.vantix.mshub.domain.port.in.AuthUseCase;
import cl.vantix.mshub.infrastructure.security.JwtService;
import cl.vantix.mshub.infrastructure.web.dto.request.*;
import cl.vantix.mshub.infrastructure.web.dto.response.*;
import cl.vantix.mshub.infrastructure.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestController @RequestMapping("/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authUseCase;
    private final JwtService jwtService;
    private final WebMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@Valid @RequestBody RegisterRequest req) {
        authUseCase.register(req.getNombre(), req.getApellidoPaterno(), req.getApellidoMaterno(),
                             req.getEmail(), req.getPassword(), req.getTelefono());
        return ResponseEntity.ok(Map.of("mensaje","Registro exitoso. Revisa tu correo para confirmar tu cuenta."));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        String token = authUseCase.login(req.getEmail(), req.getPassword());
        var usuario = authUseCase.obtenerPorEmail(req.getEmail());
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token).tipo("Bearer").email(usuario.getEmail())
                .roles(usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build());
    }

    @GetMapping("/confirmar-email")
    public ResponseEntity<Map<String,String>> confirmar(@RequestParam String token) {
        authUseCase.confirmarEmail(token);
        return ResponseEntity.ok(Map.of("mensaje","Cuenta confirmada exitosamente."));
    }

    @PostMapping("/solicitar-reset")
    public ResponseEntity<Map<String,String>> solicitarReset(@RequestBody Map<String,String> body) {
        authUseCase.solicitarResetPassword(body.get("email"));
        return ResponseEntity.ok(Map.of("mensaje","Si el correo existe, recibirás instrucciones."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,String>> resetPassword(@RequestBody Map<String,String> body) {
        authUseCase.resetPassword(body.get("token"), body.get("nuevaPassword"));
        return ResponseEntity.ok(Map.of("mensaje","Contraseña restablecida exitosamente."));
    }
}
