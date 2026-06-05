package cl.vantix.mshub.infrastructure.persistence.repository;
import cl.vantix.mshub.infrastructure.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpa, Long> {
    Optional<UsuarioJpa> findByEmail(String email);
    Optional<UsuarioJpa> findByTokenConfirmacion(String token);
    Optional<UsuarioJpa> findByTokenReset(String token);
    boolean existsByEmail(String email);
    List<UsuarioJpa> findByRoles_Nombre(String rolNombre);
}
