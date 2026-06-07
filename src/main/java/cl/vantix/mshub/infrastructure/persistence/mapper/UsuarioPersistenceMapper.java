package cl.vantix.mshub.infrastructure.persistence.mapper;

import cl.vantix.mshub.domain.model.*;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UsuarioPersistenceMapper {

    public Usuario toDomain(UsuarioJpa jpa) {
        if (jpa == null) return null;
        return Usuario.builder()
                .id(jpa.getId()).nombre(jpa.getNombre())
                .apellidoPaterno(jpa.getApellidoPaterno())
                .apellidoMaterno(jpa.getApellidoMaterno())
                .email(jpa.getEmail()).password(jpa.getPassword())
                .confirmado(jpa.isConfirmado()).activo(jpa.isActivo())
                .telefono(jpa.getTelefono()).avatarUrl(jpa.getAvatarUrl())
                .tokenConfirmacion(jpa.getTokenConfirmacion())
                .tokenExpiracion(jpa.getTokenExpiracion())
                .tokenReset(jpa.getTokenReset())
                .tokenResetExpiracion(jpa.getTokenResetExpiracion())
                .twoFactorEnabled(jpa.isTwoFactorEnabled())
                .intentosFallidos(jpa.getIntentosFallidos())
                .bloqueadoHasta(jpa.getBloqueadoHasta())
                .institucionId(jpa.getInstitucionId())
                .roles(jpa.getRoles().stream()
                        .map(r -> Rol.valueOf(r.getNombre()))
                        .collect(Collectors.toSet()))
                .creadoEn(jpa.getCreadoEn()).actualizadoEn(jpa.getActualizadoEn())
                .build();
    }

    public UsuarioJpa toJpa(Usuario domain, java.util.Set<RolJpa> rolesJpa) {
        return UsuarioJpa.builder()
                .id(domain.getId()).nombre(domain.getNombre())
                .apellidoPaterno(domain.getApellidoPaterno())
                .apellidoMaterno(domain.getApellidoMaterno())
                .email(domain.getEmail()).password(domain.getPassword())
                .confirmado(domain.isConfirmado()).activo(domain.isActivo())
                .telefono(domain.getTelefono()).avatarUrl(domain.getAvatarUrl())
                .tokenConfirmacion(domain.getTokenConfirmacion())
                .tokenExpiracion(domain.getTokenExpiracion())
                .tokenReset(domain.getTokenReset())
                .tokenResetExpiracion(domain.getTokenResetExpiracion())
                .twoFactorEnabled(domain.isTwoFactorEnabled())
                .intentosFallidos(domain.getIntentosFallidos())
                .bloqueadoHasta(domain.getBloqueadoHasta())
                .institucionId(domain.getInstitucionId())
                .roles(rolesJpa != null ? rolesJpa : new java.util.HashSet<>())
                .build();
    }
}
