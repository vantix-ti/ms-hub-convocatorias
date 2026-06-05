package cl.vantix.mshub.infrastructure.security;

import cl.vantix.mshub.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioJpaRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        var authorities = usuario.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNombre()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), usuario.getPassword(), authorities);
    }
}
