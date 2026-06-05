package cl.vantix.mshub.application.usecase;

import cl.vantix.mshub.domain.exception.BusinessException;
import cl.vantix.mshub.domain.exception.ResourceNotFoundException;
import cl.vantix.mshub.domain.exception.UnauthorizedException;
import cl.vantix.mshub.domain.model.Rol;
import cl.vantix.mshub.domain.model.Usuario;
import cl.vantix.mshub.domain.port.in.AuthUseCase;
import cl.vantix.mshub.domain.port.out.MailPort;
import cl.vantix.mshub.domain.port.out.UsuarioPersistencePort;
import cl.vantix.mshub.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUseCaseImpl implements AuthUseCase {

    private final UsuarioPersistencePort usuarioPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailPort mailPort;

    @Override
    @Transactional
    public void register(String nombre, String apellidoPaterno, String apellidoMaterno,
                         String email, String password, String telefono) {
        if (usuarioPort.existsByEmail(email))
            throw new BusinessException("El correo '" + email + "' ya está registrado.");
        String token = UUID.randomUUID().toString();
        Usuario usuario = Usuario.builder()
                .nombre(nombre).apellidoPaterno(apellidoPaterno)
                .apellidoMaterno(apellidoMaterno).email(email)
                .password(passwordEncoder.encode(password))
                .telefono(telefono).roles(Set.of(Rol.POSTULANTE))
                .confirmado(false).activo(true)
                .tokenConfirmacion(token)
                .tokenExpiracion(LocalDateTime.now().plusHours(24))
                .build();
        usuarioPort.save(usuario);
        try { mailPort.sendConfirmacionEmail(email, usuario.getNombreCompleto(), token); }
        catch (Exception ignored) {}
    }

    @Override
    @Transactional(readOnly = true)
    public String login(String email, String password) {
        Usuario usuario = usuarioPort.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas."));
        if (!usuario.isConfirmado())
            throw new UnauthorizedException("Cuenta no confirmada. Revisa tu correo.");
        if (!usuario.isActivo())
            throw new UnauthorizedException("Cuenta desactivada. Contacta al administrador.");
        if (usuario.getBloqueadoHasta() != null && LocalDateTime.now().isBefore(usuario.getBloqueadoHasta()))
            throw new UnauthorizedException("Cuenta bloqueada temporalmente. Intenta más tarde.");
        if (!passwordEncoder.matches(password, usuario.getPassword()))
            throw new UnauthorizedException("Credenciales inválidas.");
        return jwtService.generateToken(usuario);
    }

    @Override
    @Transactional
    public void confirmarEmail(String token) {
        Usuario usuario = usuarioPort.findByTokenConfirmacion(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token inválido o expirado."));
        if (usuario.getTokenExpiracion() != null && LocalDateTime.now().isAfter(usuario.getTokenExpiracion()))
            throw new BusinessException("El token ha expirado. Solicita un nuevo enlace.");
        usuario.setConfirmado(true);
        usuario.setTokenConfirmacion(null);
        usuario.setTokenExpiracion(null);
        usuarioPort.save(usuario);
    }

    @Override
    @Transactional
    public void solicitarResetPassword(String email) {
        usuarioPort.findByEmail(email).ifPresent(u -> {
            String token = UUID.randomUUID().toString();
            u.setTokenReset(token);
            u.setTokenResetExpiracion(LocalDateTime.now().plusHours(2));
            usuarioPort.save(u);
            try { mailPort.sendResetPasswordEmail(email, u.getNombreCompleto(), token); }
            catch (Exception ignored) {}
        });
    }

    @Override
    @Transactional
    public void resetPassword(String token, String nuevaPassword) {
        Usuario usuario = usuarioPort.findByTokenReset(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token inválido o expirado."));
        if (usuario.getTokenResetExpiracion() != null &&
                LocalDateTime.now().isAfter(usuario.getTokenResetExpiracion()))
            throw new BusinessException("El token ha expirado.");
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setTokenReset(null);
        usuario.setTokenResetExpiracion(null);
        // Si la cuenta no estaba confirmada (usuario creado por admin), se activa aquí
        if (!usuario.isConfirmado()) usuario.setConfirmado(true);
        usuarioPort.save(usuario);
    }


    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioPort.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
    }
}
