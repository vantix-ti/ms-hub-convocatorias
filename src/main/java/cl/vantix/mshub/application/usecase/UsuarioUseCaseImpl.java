package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.model.Usuario;
import cl.vantix.mshub.domain.port.in.UsuarioUseCase;
import cl.vantix.mshub.domain.port.out.MailPort;
import cl.vantix.mshub.domain.port.out.UsuarioPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class UsuarioUseCaseImpl implements UsuarioUseCase {
    private final UsuarioPersistencePort usuarioPort;
    private final PasswordEncoder passwordEncoder;
    private final MailPort mailPort;

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioPort.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
    }

    @Override @Transactional
    public Usuario actualizarPerfil(String email, String nombre, String apellidoPaterno,
                                    String apellidoMaterno, String telefono) {
        Usuario u = obtenerPorEmail(email);
        if (nombre != null) u.setNombre(nombre);
        if (apellidoPaterno != null) u.setApellidoPaterno(apellidoPaterno);
        if (apellidoMaterno != null) u.setApellidoMaterno(apellidoMaterno);
        if (telefono != null) u.setTelefono(telefono);
        return usuarioPort.save(u);
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) { return usuarioPort.findByRol(rol); }

    @Override
    public List<Usuario> listarTodos() { return usuarioPort.findAll(); }

    @Override @Transactional
    public void cambiarPassword(String email, String passwordActual, String nuevaPassword) {
        Usuario u = obtenerPorEmail(email);
        if (!passwordEncoder.matches(passwordActual, u.getPassword()))
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        if (nuevaPassword == null || nuevaPassword.isBlank() || nuevaPassword.length() < 8)
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres.");
        u.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioPort.save(u);
    }


    @Override @Transactional
    public Usuario crearUsuario(String nombre, String apellidoPaterno, String apellidoMaterno,
                                String email, String telefono, Rol rol, String password) {
        if (usuarioPort.existsByEmail(email))
            throw new IllegalArgumentException("Ya existe un usuario con ese correo.");

        boolean passwordManual = password != null && !password.isBlank();

        if (passwordManual) {
            // ── Rama MANUAL: contraseña definida por el admin, cuenta activa de inmediato ──
            Usuario u = Usuario.builder()
                    .nombre(nombre).apellidoPaterno(apellidoPaterno).apellidoMaterno(apellidoMaterno)
                    .email(email).telefono(telefono)
                    .password(passwordEncoder.encode(password))
                    .roles(Set.of(rol))
                    .activo(true)
                    .confirmado(true)
                    .build();
            return usuarioPort.save(u);
        } else {
            // ── Rama AUTO: token de activación 72h + email de bienvenida ──
            String placeholder = passwordEncoder.encode(UUID.randomUUID().toString());
            String token = UUID.randomUUID().toString();
            Usuario u = Usuario.builder()
                    .nombre(nombre).apellidoPaterno(apellidoPaterno).apellidoMaterno(apellidoMaterno)
                    .email(email).telefono(telefono).password(placeholder)
                    .roles(Set.of(rol))
                    .activo(true)
                    .confirmado(false)
                    .tokenReset(token)
                    .tokenResetExpiracion(LocalDateTime.now().plusHours(72))
                    .build();
            Usuario guardado = usuarioPort.save(u);
            try { mailPort.sendBienvenidaAdminEmail(email, guardado.getNombreCompleto(), token); }
            catch (Exception e) { /* log silencioso; el usuario se crea igualmente */ }
            return guardado;
        }
    }


    @Override @Transactional
    public Usuario cambiarRol(Long id, Rol rol) {
        Usuario u = usuarioPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario " + id + " no encontrado."));
        u.setRoles(Set.of(rol));
        return usuarioPort.save(u);
    }
}
